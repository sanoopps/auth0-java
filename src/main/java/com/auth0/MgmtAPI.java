package com.auth0;

import com.auth0.json.mgmt.Connection;
import com.auth0.json.mgmt.DeviceCredentials;
import com.auth0.json.mgmt.LogEvent;
import com.auth0.json.mgmt.client.Client;
import com.auth0.json.mgmt.clientgrant.ClientGrant;
import com.auth0.net.*;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.List;
import java.util.Map;

public class MgmtAPI {

    private final String baseUrl;
    private final String apiToken;
    private final OkHttpClient client;

    public MgmtAPI(String domain, String apiToken) {
        Asserts.assertNotNull(domain, "domain");
        Asserts.assertNotNull(apiToken, "api token");

        baseUrl = createBaseUrl(domain);
        if (baseUrl == null) {
            throw new IllegalArgumentException("The domain had an invalid format and couldn't be parsed as an URL.");
        }
        this.apiToken = apiToken;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    String getBaseUrl() {
        return baseUrl;
    }

    private String createBaseUrl(String domain) {
        String url = domain;
        if (!domain.startsWith("https://") && !domain.startsWith("http://")) {
            url = "https://" + domain;
        }
        HttpUrl baseUrl = HttpUrl.parse(url);
        return baseUrl == null ? null : baseUrl.newBuilder().build().toString();
    }

    /**
     * Request all the Client Grants. A token with scope read:client_grants is needed.
     *
     * @return a Request to execute.
     */
    public Request<List<ClientGrant>> listClientGrants() {
        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("client-grants")
                .build()
                .toString();
        CustomRequest<List<ClientGrant>> request = new CustomRequest<>(client, url, "GET", new TypeReference<List<ClientGrant>>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Create a new Client Grant. A token with scope create:client_grants is needed.
     *
     * @param clientId the client id to associate this grant with
     * @param audience the audience of the grant
     * @param scope    the scope to grant
     * @return a Request to execute.
     */
    public Request<ClientGrant> createClientGrant(String clientId, String audience, String[] scope) {
        Asserts.assertNotNull(clientId, "client id");
        Asserts.assertNotNull(audience, "audience");
        Asserts.assertNotNull(scope, "scope");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("client-grants")
                .build()
                .toString();
        CustomRequest<ClientGrant> request = new CustomRequest<>(client, url, "POST", new TypeReference<ClientGrant>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.addParameter("client_id", clientId);
        request.addParameter("audience", audience);
        request.addParameter("scope", scope);
        return request;
    }


    /**
     * Delete an existing Client Grant. A token with scope delete:client_grants is needed.
     *
     * @param clientGrantId the client grant id
     * @return a Request to execute.
     */
    public Request deleteClientGrant(String clientGrantId) {
        Asserts.assertNotNull(clientGrantId, "client grant id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("client-grants")
                .addPathSegment(clientGrantId)
                .build()
                .toString();
        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Update an existing Client Grant. A token with scope update:client_grants is needed.
     *
     * @param clientGrantId the client grant id
     * @param scope         the scope to grant
     * @return a Request to execute.
     */
    public Request<ClientGrant> updateClientGrant(String clientGrantId, String[] scope) {
        Asserts.assertNotNull(clientGrantId, "client grant id");
        Asserts.assertNotNull(scope, "scope");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("client-grants")
                .addPathSegment(clientGrantId)
                .build()
                .toString();
        CustomRequest<ClientGrant> request = new CustomRequest<>(client, url, "PATCH", new TypeReference<ClientGrant>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.addParameter("scope", scope);
        return request;
    }

    /**
     * Request all the Clients. A token with scope read:clients is needed. If you also need the client_secret and encryption_key attributes the token must have read:client_keys scope.
     *
     * @return a Request to execute.
     */
    public Request<List<Client>> listClients() {
        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .build()
                .toString();
        CustomRequest<List<Client>> request = new CustomRequest<>(client, url, "GET", new TypeReference<List<Client>>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Request a Client. A token with scope read:clients is needed. If you also need the client_secret and encryption_key attributes the token must have read:client_keys scope.
     *
     * @param clientId the client id
     * @return a Request to execute.
     */
    public Request<Client> getClient(String clientId) {
        Asserts.assertNotNull(clientId, "client id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .addPathSegment(clientId)
                .build()
                .toString();
        CustomRequest<Client> request = new CustomRequest<>(client, url, "GET", new TypeReference<Client>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Create a new Client. A token with scope create:clients is needed.
     *
     * @param client the client data to set
     * @return a Request to execute.
     */
    public Request<Client> createClient(Client client) {
        Asserts.assertNotNull(client, "client");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .build()
                .toString();
        CustomRequest<Client> request = new CustomRequest<>(this.client, url, "POST", new TypeReference<Client>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.setBody(client);
        return request;
    }

    /**
     * Delete an existing Client. A token with scope delete:clients is needed.
     *
     * @param clientId the client id
     * @return a Request to execute.
     */
    public Request deleteClient(String clientId) {
        Asserts.assertNotNull(clientId, "client id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .addPathSegment(clientId)
                .build()
                .toString();
        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Update an existing Client. A token with scope update:clients is needed. If you also need to update the client_secret and encryption_key attributes the token must have update:client_keys scope.
     *
     * @param clientId the client id
     * @param client   the client data to set
     * @return a Request to execute.
     */
    public Request<Client> updateClient(String clientId, Client client) {
        Asserts.assertNotNull(clientId, "client id");
        Asserts.assertNotNull(client, "client");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .addPathSegment(clientId)
                .build()
                .toString();
        CustomRequest<Client> request = new CustomRequest<>(this.client, url, "PATCH", new TypeReference<Client>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.setBody(client);
        return request;
    }

    /**
     * Rotates a Client secret. A token with scope update:client_keys is needed. Note that the generated secret is NOT base64 encoded.
     *
     * @param clientId the client id
     * @return a Request to execute.
     */
    public Request<Client> rotateClientSecret(String clientId) {
        Asserts.assertNotNull(clientId, "client id");
        Asserts.assertNotNull(client, "client");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("clients")
                .addPathSegment(clientId)
                .addPathSegment("rotate-secret")
                .build()
                .toString();
        CustomRequest<Client> request = new EmptyBodyRequest<>(this.client, url, "POST", new TypeReference<Client>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }


    /**
     * Request all the Connections. A token with scope read:connections is needed.
     *
     * @param filter the filter to use. Can be null
     * @return a Request to execute.
     */
    public Request<List<Connection>> listConnections(ConnectionFilter filter) {
        HttpUrl.Builder builder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections");
        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }
        String url = builder.build().toString();
        CustomRequest<List<Connection>> request = new CustomRequest<>(client, url, "GET", new TypeReference<List<Connection>>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Request a Connection. A token with scope read:connections is needed.
     *
     * @param connectionId the id of the connection to retrieve.
     * @param filter       the filter to use. Can be null
     * @return a Request to execute.
     */
    public Request<Connection> getConnection(String connectionId, ConnectionFilter filter) {
        Asserts.assertNotNull(connectionId, "connection id");

        HttpUrl.Builder builder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections")
                .addPathSegment(connectionId);
        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }
        String url = builder.build().toString();
        CustomRequest<Connection> request = new CustomRequest<>(client, url, "GET", new TypeReference<Connection>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Create a new Connection. A token with scope create:connections is needed.
     *
     * @param connection the connection data to set
     * @return a Request to execute.
     */
    public Request<Connection> createConnection(Connection connection) {
        Asserts.assertNotNull(connection, "connection");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections")
                .build()
                .toString();
        CustomRequest<Connection> request = new CustomRequest<>(this.client, url, "POST", new TypeReference<Connection>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.setBody(connection);
        return request;
    }

    /**
     * Delete an existing Connection. A token with scope delete:connections is needed.
     *
     * @param connectionId the connection id
     * @return a Request to execute.
     */
    public Request deleteConnection(String connectionId) {
        Asserts.assertNotNull(connectionId, "connection id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections")
                .addPathSegment(connectionId)
                .build()
                .toString();
        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Update an existing Connection. A token with scope update:connections is needed. Note that if the 'options' value is present it will override all the 'options' values that currently exist.
     *
     * @param connectionId the connection id
     * @param connection   the connection data to set. It can't include name or strategy.
     * @return a Request to execute.
     */
    public Request<Connection> updateConnection(String connectionId, Connection connection) {
        Asserts.assertNotNull(connectionId, "connection id");
        Asserts.assertNotNull(connection, "connection");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections")
                .addPathSegment(connectionId)
                .build()
                .toString();
        CustomRequest<Connection> request = new CustomRequest<>(this.client, url, "PATCH", new TypeReference<Connection>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.setBody(connection);
        return request;
    }

    /**
     * Delete an existing User from the given Database Connection. A token with scope delete:users is needed.
     *
     * @param connectionId the connection id where the user is stored
     * @param email        the email of the user to delete
     * @return a Request to execute.
     */
    public Request deleteConnectionUser(String connectionId, String email) {
        Asserts.assertNotNull(connectionId, "connection id");
        Asserts.assertNotNull(email, "email");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("connections")
                .addPathSegment(connectionId)
                .addPathSegment("users")
                .addQueryParameter("email", email)
                .build()
                .toString();
        VoidRequest request = new VoidRequest(this.client, url, "DELETE");
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Request all the Device Credentials. A token with scope read:device_credentials is needed.
     *
     * @param filter the filter to use. Can be null
     * @return a Request to execute.
     */
    public Request<List<DeviceCredentials>> listDeviceCredentials(DeviceCredentialsFilter filter) {
        HttpUrl.Builder builder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("device-credentials");
        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }
        String url = builder.build().toString();
        CustomRequest<List<DeviceCredentials>> request = new CustomRequest<>(client, url, "GET", new TypeReference<List<DeviceCredentials>>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Create a new Device Credentials. A token with scope create:current_user_device_credentials is needed.
     *
     * @param deviceCredentials the device credentials data to set
     * @return a Request to execute.
     */
    public Request<DeviceCredentials> createDeviceCredentials(DeviceCredentials deviceCredentials) {
        Asserts.assertNotNull(deviceCredentials, "device credentials");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("device-credentials")
                .build()
                .toString();
        CustomRequest<DeviceCredentials> request = new CustomRequest<>(this.client, url, "POST", new TypeReference<DeviceCredentials>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        request.setBody(deviceCredentials);
        return request;
    }

    /**
     * Delete an existing Device Credentials. A token with scope delete:device_credentials is needed.
     *
     * @param deviceCredentialsId the device credentials id
     * @return a Request to execute.
     */
    public Request deleteDeviceCredentials(String deviceCredentialsId) {
        Asserts.assertNotNull(deviceCredentialsId, "device credentials id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("device-credentials")
                .addPathSegment(deviceCredentialsId)
                .build()
                .toString();
        VoidRequest request = new VoidRequest(client, url, "DELETE");
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }


    /**
     * Request all the Log Events. A token with scope read:logs is needed.
     *
     * @param filter the filter to use. Can be null
     * @return a Request to execute.
     */
    public Request<List<LogEvent>> listLogEvents(LogEventFilter filter) {
        HttpUrl.Builder builder = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("logs");
        if (filter != null) {
            for (Map.Entry<String, Object> e : filter.getAsMap().entrySet()) {
                builder.addQueryParameter(e.getKey(), String.valueOf(e.getValue()));
            }
        }
        String url = builder.build().toString();
        CustomRequest<List<LogEvent>> request = new CustomRequest<>(client, url, "GET", new TypeReference<List<LogEvent>>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }

    /**
     * Request a Log Event. A token with scope read:logs is needed.
     *
     * @param logEventId the id of the connection to retrieve.
     * @return a Request to execute.
     */
    public Request<LogEvent> getLogEvent(String logEventId) {
        Asserts.assertNotNull(logEventId, "log event id");

        String url = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("logs")
                .addPathSegment(logEventId)
                .build()
                .toString();
        CustomRequest<LogEvent> request = new CustomRequest<>(client, url, "GET", new TypeReference<LogEvent>() {
        });
        request.addHeader("Authorization", "Bearer " + apiToken);
        return request;
    }
}