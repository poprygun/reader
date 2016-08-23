package io.pivotal.poc.cs;

import com.hazelcast.client.HazelcastClient;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ReaderServiceImpl implements ReaderService {
    private static final Logger LOG = LoggerFactory.getLogger(ReaderServiceImpl.class);

//    @Autowired
    private HazelcastInstance hazelcastInstance;
    private static final int RECEICED_MESSAGES_TRACK_TTL_SECS = 60 * 60;


    public static final String ACCEPTED_MESSAGES_TRACKING_MAP_NAME = "received";
    public static final String RECIPIENT_QUEUE_NAME_SUFFIX = "recipient-";

    @PostConstruct
    public void init() throws ExecutionException {
//        Config config = new Config();
//        config.addMapConfig(
//                // Set up TTL for the Map tracking received Messages IDs
//                new MapConfig()
//                        .setName(ReaderServiceImpl.ACCEPTED_MESSAGES_TRACKING_MAP_NAME)
//                        .setEvictionPolicy(EvictionPolicy.LRU)
//                        .setTimeToLiveSeconds(RECEICED_MESSAGES_TRACK_TTL_SECS));
//        config.getNetworkConfig()
//                .setPublicAddress("192.168.11.1")
//                .setPort(5701);
        ClientConfig config = new ClientConfig();
//        config.getNetworkConfig().addAddress("192.168.11.1:5701");
//        config.getNetworkConfig().addAddress("10.254.0.26:5701");
        config.getNetworkConfig().addAddress("10.254.0.50:5701");
        hazelcastInstance = HazelcastClient.newHazelcastClient(config);

    }
//    @Autowired
//    public ReaderServiceImpl(HazelcastInstance hazelcastInstance) {
//        this.hazelcastInstance = hazelcastInstance;
//    }

    private IQueue<DataMessage> recipientQueue(String user) {
        return hazelcastInstance.getQueue(RECIPIENT_QUEUE_NAME_SUFFIX + user);
    }


    private IMap<Object, Object> acceptedMessageUidMap() {
        return hazelcastInstance.getMap(ACCEPTED_MESSAGES_TRACKING_MAP_NAME);
    }


    @Override
    public List<DataMessage> receive(String recipient) {

        // Poll recipient's queue until empty
        final List<DataMessage> messages = new ArrayList();
        while ( true ) {
            final DataMessage message = recipientQueue(recipient).poll();
            if ( message == null ) break;
            LOG.info("----->Polled message id:{}", message.getMessageUid());
            messages.add(message);
        }

        return Collections.unmodifiableList(messages);
    }

    @Override
    public void readMap() {
        IMap map = hazelcastInstance.getMap("names");
        Collection values = map.values();
        for (Iterator iterator = values.iterator(); iterator.hasNext(); ) {
            Object next = iterator.next();
            LOG.info(next.toString());
        }
        System.out.println("Map Size:" + map.size());

    }

    private boolean isDuplicate(DataMessage message) {
        // We just store and check the message UID. A distributed Set would suffice, but unfortunately
        // Hazelcast ISet doesn't support automatic eviction
        final boolean duplicate = acceptedMessageUidMap().containsKey(message.getMessageUid());
        LOG.debug("Message id:{} is duplicate? {}", message.getMessageUid(), duplicate);
        return duplicate;
    }

}
