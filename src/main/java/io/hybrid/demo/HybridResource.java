package io.hybrid.demo;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@Path("/")
public class HybridResource {

    @ConfigProperty(name = "cluster.name", defaultValue = "unknown")
    String clusterName;

    @ConfigProperty(name = "cluster.role", defaultValue = "unknown")
    String clusterRole;

    @ConfigProperty(name = "cluster.region", defaultValue = "unknown")
    String clusterRegion;

    @ConfigProperty(name = "app.version", defaultValue = "dev")
    String appVersion;

    @ConfigProperty(name = "app.git-sha", defaultValue = "local")
    String gitSha;

    @ConfigProperty(name = "app.message", defaultValue = "Hello from the hybrid demo!")
    String message;

    @Inject
    @Location("index.html")
    Template index;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        return index
                .data("clusterName", clusterName)
                .data("clusterRole", clusterRole)
                .data("clusterRegion", clusterRegion)
                .data("appVersion", appVersion)
                .data("gitSha", gitSha)
                .data("message", message);
    }

    @GET
    @Path("/api/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> info() {
        return Map.of(
                "cluster", clusterName,
                "role", clusterRole,
                "region", clusterRegion,
                "version", appVersion,
                "gitSha", gitSha,
                "message", message);
    }
}
