package ru.sbt.ds;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by Devjiu on 5/12/2017.
 */
@Service
public class LocalZuulFilter extends ZuulFilter {
    @Autowired
    private EurekaClient eurekaClient;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 99;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        InstanceInfo instanceGoodVersion = (InstanceInfo) eurekaClient.getApplications().getRegisteredApplications()
                .stream()
                .filter(x -> Objects.equals(x.getName(), "PHOTO-SERVICE-CLIENT"))
                .map(x -> x.getInstances()
                        .stream()
                        .filter(z -> new PingUrl(false, "/health")
                                .isAlive( new Server(z.getIPAddr(), z.getPort())
                                ))
                        .max(Comparator.comparingInt(y -> Integer.parseInt(y.getMetadata().get("version"))))
                        .get()
                )
                .toArray()[0];

        try {
            ctx.setRouteHost(new URL("http://" + instanceGoodVersion.getIPAddr() + ":" + instanceGoodVersion.getPort()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("======================Version number: " + instanceGoodVersion.getMetadata().get("version") + "======================");
        return null;
    }
}
