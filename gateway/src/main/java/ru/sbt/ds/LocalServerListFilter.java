package ru.sbt.ds;

import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devjiu on 5/12/2017.
 */
@Service
public class LocalServerListFilter implements ServerListFilter<Server> {

    @Override
    public List<Server> getFilteredListOfServers(List<Server> list) {


        List<Server> aliveServers = new ArrayList<>();
        for (Server server: list) {
            /*String version = server.getMetaInfo().getInstanceId();
            System.out.println("======================Version number: " + version + "======================");*/
            if (new PingUrl(false, "/health").isAlive(server)) {
                aliveServers.add(server);
                break;
            }
        }
        return aliveServers;
    }
}
