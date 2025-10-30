package com.kangaroohy.milo.pool;

import com.kangaroohy.milo.configuration.MiloProperties;
import com.kangaroohy.milo.exception.EndPointNotFoundException;
import com.kangaroohy.milo.exception.IdentityNotFoundException;
import com.kangaroohy.milo.utils.CustomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 类 MiloConnectFactory 功能描述：<br/>
 *
 * @author mes
 * @version 0.0.1
 * @date 2023/5/4 18:56
 */
@Slf4j
public class MiloConnectFactory implements KeyedPooledObjectFactory<MiloProperties.Config, OpcUaClient> {

    public MiloConnectFactory(MiloProperties properties) {
        CustomUtil.verifyProperties(properties);
    }

    /**
     * 创建对象
     *
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<OpcUaClient> makeObject(MiloProperties.Config key) throws Exception {
        OpcUaClient client = null;
        try {
            client = createClient(key);
            client.connect().get();
            return new DefaultPooledObject<>(client);
        } catch (Exception e) {
            if (client != null) {
                client.disconnect().get();
            }
            throw new InterruptedException(e.getMessage());
        }
    }

    /**
     * 对象要被销毁时(validateObject方法返回false或者超时)后被调用
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void destroyObject(MiloProperties.Config key, PooledObject<OpcUaClient> pooledObject) throws Exception {
        OpcUaClient opcUaClient = pooledObject.getObject();
        log.info("disconnect opcUaClient {}", opcUaClient.getConfig().getApplicationName().getText());
        opcUaClient.disconnect().get();
    }

    /**
     * 每次获取对象和还回对象时会被调用，如果返回false会销毁对象
     */
    @Override
    public boolean validateObject(MiloProperties.Config key, PooledObject<OpcUaClient> pooledObject) {
        return true;
    }

    /**
     * 调用获取对象方法前被调用
     * 此方法一般进行一些前置操作
     */
    @Override
    public void activateObject(MiloProperties.Config key, PooledObject<OpcUaClient> pooledObject) throws Exception {

    }

    /**
     * 当还回对象并且validateObject方法返回true后被调用
     * 一般在此方法中对刚刚使用完成的对象进行重置
     */
    @Override
    public void passivateObject(MiloProperties.Config key, PooledObject<OpcUaClient> pooledObject) throws Exception {

    }

    private OpcUaClient createClient(MiloProperties.Config key) throws Exception {
        Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
        Files.createDirectories(securityTempDir);
        if (!Files.exists(securityTempDir)) {
            throw new Exception("unable to create security dir: " + securityTempDir);
        }
        return OpcUaClient.create(key.getEndpoint(),
                endpoints -> {
                    final Optional<EndpointDescription> endpoint = endpoints
                            .stream()
//                            .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                            .findFirst();
                    EndpointDescription newEndpoint = new EndpointDescription(key.getEndpoint(), endpoint.get().getServer(), endpoint.get().getServerCertificate(),
                            endpoint.get().getSecurityMode(), endpoint.get().getSecurityPolicyUri(), endpoint.get().getUserIdentityTokens(),
                            endpoint.get().getTransportProfileUri(), endpoint.get().getSecurityLevel());
                    return Optional.of(newEndpoint);
                },
                configBuilder ->
                        configBuilder
                                .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                                .setApplicationUri("urn:eclipse:milo:examples:client")
                                //访问方式
                                .setIdentityProvider(new UsernameProvider(key.getUsername(), key.getPassword()))
                                .setRequestTimeout(UInteger.valueOf(5000))
                                .build()
        );
    }

    private URI getUri(MiloProperties.Config key) {
        try {
            return new URI(endpointUrl(key));
        } catch (URISyntaxException e) {
            throw new EndPointNotFoundException("endpoint 配置异常");
        }
    }

    private String endpointUrl(MiloProperties.Config key) {
        return key.getEndpoint();
    }

    private SecurityPolicy securityPolicy(MiloProperties.Config key) {
        return key.getSecurityPolicy();
    }

    private IdentityProvider identityProvider(MiloProperties.Config key) {
        if (securityPolicy(key).equals(SecurityPolicy.None)) {
            return new AnonymousProvider();
        }
        if (key.getUsername() == null || key.getPassword() == null) {
            throw new IdentityNotFoundException("连接信息未完善");
        } else {
            return new UsernameProvider(key.getUsername(), key.getPassword());
        }
    }
}
