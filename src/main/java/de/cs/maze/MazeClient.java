package de.cs.maze;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;

/**
 * http://www.epdeveloperchallenge.com/
 */
public class MazeClient {

  private static final String BASE_URI = "http://www.epdeveloperchallenge.com/";

  private static final String PATH_PREFIX = "/api/";

  private WebTarget target;

  public MazeClient() {
    ClientConfig config = new ClientConfig();
    config.register(JacksonJsonProvider.class);
    Client client = ClientBuilder.newClient(config);
    target = client.target(BASE_URI);
  }

  public MazeCell init() {
    MazeCell response = post("init", new Form());
    return response;
  }

  public MazeCell move(String mazeGuid, Direction direction) {
    Form form = new Form();
    form.param("mazeGuid", mazeGuid);
    form.param("direction", direction.toString());
    MazeCell response = post("move", form);
    return response;
  }

  public MazeCell jump(String mazeGuid, int x, int y) {
    Form form = new Form();
    form.param("mazeGuid", mazeGuid);
    form.param("x", String.valueOf(x));
    form.param("y", String.valueOf(y));
    MazeCell response = post("jump", form);
    return response;
  }

  public MazeCell currentCell(String mazeGuid) {
    Builder requestBuilder = target.queryParam("mazeGuid", mazeGuid)
        .path(PATH_PREFIX + "currentCell").request()
        .accept(MediaType.APPLICATION_JSON);
    MazeResponse mazeResponse = requestBuilder.get(MazeResponse.class);
    return mazeResponse.getCurrentCell();
  }

  private MazeCell post(String path, Form payload) {
    Builder requestBuilder = createRequestBuilder(path);
    MazeResponse mazeResponse = requestBuilder.post(Entity.form(payload),
        MazeResponse.class);
    return mazeResponse.getCurrentCell();
  }

  private Builder createRequestBuilder(String path) {
    Builder builder = target.path(PATH_PREFIX + path).request()
        .accept(MediaType.APPLICATION_JSON);
    return builder;
  }
}
