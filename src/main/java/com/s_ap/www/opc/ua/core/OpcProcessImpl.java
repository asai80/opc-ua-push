package com.s_ap.www.opc.ua.core;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscriptionManager;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s_ap.www.opc.ua.global.Constant;
import com.s_ap.www.opc.ua.util.KeyStoreLoader;

/**
 * 
 * @author zihaozhu
 * @date 2017-11-29 11:24:13 AM
 */
public class OpcProcessImpl extends OpcProcess {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final KeyStoreLoader loader = new KeyStoreLoader();
	private OpcUaClient client;
	/** 一个组一个订阅管理器 */
	private UaSubscription subscription;
	/** 一个组多个订阅管理器 */
	private Map<String, UaSubscription> subscriptions = new ConcurrentHashMap<String, UaSubscription>();

	private final AtomicLong clientHandles = new AtomicLong(1L);

	private OpcUaClient createClient() throws Exception {
		SecurityPolicy securityPolicy = SecurityPolicy.None;
		IdentityProvider identityProvider = new AnonymousProvider();

		EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(Constant.URL).get();

		EndpointDescription endpoint = Arrays.stream(endpoints)
				.filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getSecurityPolicyUri())).findFirst()
				.orElseThrow(() -> new Exception("no desired endpoints returned"));

		logger.info("Using endpoint: {} [{}]", endpoint.getEndpointUrl(), securityPolicy);

		OpcUaClientConfig config = OpcUaClientConfig.builder()
				.setApplicationName(LocalizedText.english("custom opc-ua client"))
				.setApplicationUri("urn:eclipse:custom:examples:client").setCertificate(loader.getClientCertificate())
				.setKeyPair(loader.getClientKeyPair()).setEndpoint(endpoint).setIdentityProvider(identityProvider)
				.setRequestTimeout(uint(5000)).build();

		return new OpcUaClient(config);
	}

	private void onSubscriptionValue(UaMonitoredItem item, DataValue value) {
		NodeId nodeId = item.getReadValueId().getNodeId();
		String identifier = nodeId.getIdentifier().toString();
		Object val = value.getValue().getValue();

		if (null != this.getPoints()) {

			this.getPoints().forEach((point) -> {
				if (identifier == point.getName()) {
					point.setValue(val);
					point.setActive(true);
					point.setClientHandle(item.getClientHandle().toString());
					point.setQuality(value.getStatusCode().isGood());
					point.setTimestamp(value.getServerTime().toString());

					logger.info("点变化了{},{}", point);

					/** 触发点变化事件 */
					point.triggerPointChange(point);
				}
			});
		}
	}

	@Override
	public boolean connect() {
		boolean result = false;
		try {
			client = createClient();
			client.connect().get();

			CompletableFuture<UaClient> exceptionally = client.connect().exceptionally(throwable -> {
				throwable.printStackTrace();
				return null;
			});

			if (null != exceptionally.get()) {
				result = true;
			} else {
				client = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void disconn() {
		client.disconnect();

	}

	@Override
	public Object read(Point p) {
		Object result = null;
		if (!checkPointExists(p)) {
			return null;
		}

		NodeId nodeId = new NodeId(2, p.getName());

		CompletableFuture<DataValue> readValue = client.readValue(0, TimestampsToReturn.Both, nodeId);
		try {
			result = readValue.get().getValue().getValue();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public boolean write(Point p, String value) {
		boolean result = false;
		if (!checkPointExists(p)) {
			return false;
		}

		NodeId nodeId = new NodeId(2, p.getName());

		try {
			Object object = PointType.getValueType(p.getPointType(), value);

			CompletableFuture<StatusCode> writeValue = client.writeValue(nodeId,
					new DataValue(new Variant(object), null, null, null));

			result = writeValue.get().isGood();

			if (!result) {
				logger.error("{} write value={} error!", p.getName(), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Map<String, Object> read(Group g) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (g.getPoints().isEmpty()) {
			return null;
		}
		for (int i = 0; i < g.getPoints().size(); i++) {
			Object read = this.read(g.getPoints().get(i));
			map.put(g.getPoints().get(i).getName(), read);
		}

		return map;
	}

	@Override
	public boolean write(Group g, List<String> value) {
		boolean result = true;
		if (g.getPoints().isEmpty() || g.getPoints().size() != value.size()) {
			return false;
		}

		for (int i = 0; i < g.getPoints().size(); i++) {
			Point point = g.getPoints().get(i);
			boolean write = this.write(point, value.get(i));
			if (!write) {
				result = false;
			}
		}

		return result;
	}

	@Override
	public List<String> browserNode(String name) {
		List<String> list = new ArrayList<String>();

		try {
			NodeId nodeId = new NodeId(2, name);

			List<Node> nodes = client.getAddressSpace().browse(nodeId).get();

			for (Node node : nodes) {
				String subNodeName = node.getBrowseName().get().getName();

				if (!subNodeName.startsWith("_")) {
					list.add(subNodeName);
					logger.info("{} Node={}", name, subNodeName);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", name, e.getMessage(), e);
		}

		return list;
	}

	@Override
	public void subscribe(Point p) {
		if (!checkPointExists(p)) {
			return;
		}

		try {
			NodeId nodeId = new NodeId(2, p.getName());

			// create a subscription @ 1000ms
			UaSubscription subscription = client.getSubscriptionManager().createSubscription(1000.0).get();
			subscriptions.put(p.getName(), subscription);

			ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

			// important: client handle must be unique per item
			UInteger clientHandle = uint(clientHandles.getAndIncrement());

			MonitoringParameters parameters = new MonitoringParameters(clientHandle, 1000.0, // sampling interval
					null, // filter, null means use default
					uint(10), // queue size
					true // discard oldest
			);

			MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting,
					parameters);

			BiConsumer<UaMonitoredItem, Integer> onItemCreated = (item, id) -> item
					.setValueConsumer(this::onSubscriptionValue);

			List<UaMonitoredItem> items = subscription
					.createMonitoredItems(TimestampsToReturn.Both, newArrayList(request), onItemCreated).get();

			for (UaMonitoredItem item : items) {
				if (item.getStatusCode().isGood()) {
					logger.info("item created for nodeId={}", item.getReadValueId().getNodeId());
				} else {
					logger.warn("failed to create item for nodeId={} (status={})", item.getReadValueId().getNodeId(),
							item.getStatusCode());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void subscribeMoreSubscription(Group g) {
		if (g.getPoints().isEmpty()) {
			return;
		}

		for (int i = 0; i < g.getPoints().size(); i++) {
			Point point = g.getPoints().get(i);
			this.subscribe(point);
		}
	}

	@Override
	public void subscribeOneSubscription(Group g) {
		if (g.getPoints().isEmpty()) {
			return;
		}

		try {
			List<MonitoredItemCreateRequest> monitoredItemCreateRequests = new ArrayList<>();

			for (int i = 0; i < g.getPoints().size(); i++) {
				Point p = g.getPoints().get(i);

				if (!checkPointExists(p)) {
					continue;
				}

				NodeId nodeId = new NodeId(2, p.getName());

				// create a subscription @ 1000ms
				if (null == subscription) {
					subscription = client.getSubscriptionManager().createSubscription(1000.0).get();
				}

				ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null,
						QualifiedName.NULL_VALUE);

				// important: client handle must be unique per item
				UInteger clientHandle = uint(clientHandles.getAndIncrement());

				MonitoringParameters parameters = new MonitoringParameters(clientHandle, 1000.0, // sampling interval
						null, // filter, null means use default
						uint(10), // queue size
						true // discard oldest
				);

				MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId,
						MonitoringMode.Reporting, parameters);
				monitoredItemCreateRequests.add(request);

			}

			BiConsumer<UaMonitoredItem, Integer> onItemCreated = (item, id) -> item
					.setValueConsumer(this::onSubscriptionValue);

			List<UaMonitoredItem> items = subscription
					.createMonitoredItems(TimestampsToReturn.Both, monitoredItemCreateRequests, onItemCreated).get();

			for (UaMonitoredItem item : items) {
				if (item.getStatusCode().isGood()) {
					logger.info("item created for nodeId={}", item.getReadValueId().getNodeId());
				} else {
					logger.warn("failed to create item for nodeId={} (status={})", item.getReadValueId().getNodeId(),
							item.getStatusCode());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void cancelSubscribe(Point p) {
		OpcUaSubscriptionManager subscriptionManager = client.getSubscriptionManager();

		if (null != subscription) {
			subscriptionManager.deleteSubscription(subscription.getSubscriptionId());
		}

		if (!subscriptions.isEmpty()) {
			for (int i = 0; i < subscriptions.size(); i++) {
				Set<String> keySet = subscriptions.keySet();
				if (keySet.contains(p.getName())) {
					subscriptionManager.deleteSubscription(subscriptions.get(p.getName()).getSubscriptionId());
					subscriptions.remove(p.getName());
				}
			}
		}

		// logger.info("->" + subscriptionManager.getSubscriptions().size());
	}

	@Override
	public void cancelSubscribe(Group g) {
		OpcUaSubscriptionManager subscriptionManager = client.getSubscriptionManager();
		if (null != subscription) {
			subscriptionManager.deleteSubscription(subscription.getSubscriptionId());
		}

		if (!subscriptions.isEmpty()) {
			for (int i = 0; i < subscriptions.size(); i++) {
				Set<String> keySet = subscriptions.keySet();
				keySet.forEach(action -> {
					subscriptionManager.deleteSubscription(subscriptions.get(action).getSubscriptionId());
				});
			}

			subscriptions.clear();
		}

		// logger.info("->" + subscriptionManager.getSubscriptions().size());
	}

	@Override
	public boolean checkPointExists(Point p) {
		return true;
	}

	@Override
	public void addFaultListener() {
		client.addFaultListener(serviceFault -> {
			logger.error(serviceFault.toString());
		});
	}
}
