package com.example.tutorialws4.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
// Интерфейс WebSocketMessageBrokerConfigurer определяет способ обработки и настройки сообщений с помощью STOMP
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * <p>Давайте представим схему простого чата, где пользователи могут обмениваться сообщениями через веб-приложение. </p>
     * <p>Предположим, у нас есть два пользователя: Alice и Bob. Каждый из них подключается к веб-приложению для обмена сообщениями.</p>
     * <p>Пример использования каждой из настроек <code>registry</code> на этой схеме:</p>
     * <pre><code class="lang-java"><span class="hljs-variable">@Configuration</span>
     * <span class="hljs-variable">@EnableWebSocketMessageBroker</span>
     * public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
     *
     *     <span class="hljs-variable">@Override</span>
     *     public void configureMessageBroker(MessageBrokerRegistry registry) {
     *         <span class="hljs-comment">// Включаем простой брокер сообщений с адресом /user</span>
     *         <span class="hljs-selector-tag">registry</span><span class="hljs-selector-class">.enableSimpleBroker</span>(<span class="hljs-string">"/user"</span>);
     *
     *         <span class="hljs-comment">// Задаем префикс назначения приложения</span>
     *         <span class="hljs-selector-tag">registry</span><span class="hljs-selector-class">.setApplicationDestinationPrefixes</span>(<span class="hljs-string">"/app"</span>);
     *
     *         <span class="hljs-comment">// Указываем префикс назначения пользователя</span>
     *         <span class="hljs-selector-tag">registry</span><span class="hljs-selector-class">.setUserDestinationPrefix</span>(<span class="hljs-string">"/user"</span>);
     *     }
     *
     *     <span class="hljs-selector-tag">@Override</span>
     *     <span class="hljs-selector-tag">public</span> <span class="hljs-selector-tag">void</span> <span class="hljs-selector-tag">registerStompEndpoints</span>(StompEndpointRegistry registry) {
     *         <span class="hljs-comment">// Регистрируем конечную точку для WebSocket соединения</span>
     *         <span class="hljs-selector-tag">registry</span><span class="hljs-selector-class">.addEndpoint</span>(<span class="hljs-string">"/chat"</span>)<span class="hljs-selector-class">.withSockJS</span>();
     *     }
     * }
     * </code></pre>
     * <p>Теперь давайте посмотрим, как каждая из настроек используется во время обмена сообщениями:</p>
     * <ol>
     * <li><p>Простой брокер сообщений с адресом <code>/user</code>:</p>
     * <ul>
     * <li>Когда Alice отправляет сообщение, оно маршрутизируется по адресу <code>/user</code> и доставляется всем подписчикам, зарегистрированным на этом адресе, то есть Bob.</li>
     * <li>Аналогично, когда Bob отправляет сообщение, оно также маршрутизируется по адресу <code>/user</code> и доставляется Alice.</li>
     * </ul>
     * </li>
     * <li><p>Префикс назначения приложения <code>/app</code>:</p>
     * <ul>
     * <li>Когда Alice хочет отправить сообщение, она отправляет его с префиксом <code>/app</code>, например <code>/app/chat.sendMessage</code>.</li>
     * <li>Сервер обрабатывает это сообщение, выполняя соответствующие действия, например, отправляет сообщение всем активным участникам чата.</li>
     * </ul>
     * </li>
     * <li><p>Префикс назначения пользователя <code>/user</code>:</p>
     * <ul>
     * <li>Когда Alice хочет отправить персонализированное сообщение Bob, она отправляет его с префиксом <code>/user/Bob</code>, например <code>/user/Bob/chat.privateMessage</code>.</li>
     * <li>Сервер маршрутизирует это сообщение напрямую Bob, так как оно адресовано только ему.</li>
     * </ul>
     * </li>
     * </ol>
     * <p>Таким образом, эти настройки позволяют реализовать гибкую систему обмена сообщениями, обеспечивая как общий, так и персонализированный пользовательский опыт в приложении чата.</p>
     */
    //    Конфигурация посредника обмена сообщений
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        Этот метод включает простой брокер сообщений с определенным адресом. В данном случае, сообщения будут отправляться всем подписчикам, зарегистрированным на адрес /user.
        registry.enableSimpleBroker("/user");
//        Этот метод задает префикс для назначения приложения. Это означает, что все сообщения, адресованные клиентом с префиксом /app, будут передаваться на обработку в приложение.
        registry.setApplicationDestinationPrefixes("/app");

//        Устанавливается префикс назначения для пользователей в виде /user/{userId}/**. Это означает, что все сообщения,
//        адресованные конкретному пользователю, будут отправляться по адресу, который начинается с /user/,
//        после чего идет динамический идентификатор пользователя ({userId}), и, возможно, дополнительные параметры (/**).
//
//        Например, если мы хотим отправить сообщение пользователю с идентификатором 123,
//        то адрес назначения будет выглядеть как /user/123/**. Дополнительные параметры могут быть добавлены после
//        идентификатора пользователя, если необходимо уточнить адрес назначения сообщения.
//
//        Этот метод полезен, когда нужно обеспечить доставку сообщений конкретным пользователям в приложении WebSocket,
//        позволяя использовать динамические идентификаторы для адресации сообщений.
//        registry.setUserDestinationPrefix("/user/{userId}/**");
//        registry.setUserDestinationPrefix("/{userId}/**");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * <p>Этот код является методом конфигурации конвертеров сообщений</p>
     * <ol>
     * <li><p><code>public boolean configureMessageConverters(List&lt;MessageConverter&gt; messageConverters)</code>: Этот метод используется для настройки конвертеров сообщений. Он принимает список <code>messageConverters</code>, в который добавляются конвертеры сообщений.</p>
     * </li>
     * <li><p><code>DefaultContentTypeResolver resolver = new DefaultContentTypeResolver()</code>: Создается экземпляр объекта <code>DefaultContentTypeResolver</code>, который используется для определения MIME-типов сообщений.</p>
     * </li>
     * <li><p><code>resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON)</code>: Устанавливается MIME-тип сообщений по умолчанию. В данном случае, устанавливается MIME-тип <code>application/json</code>, что означает, что сообщения будут представлены в формате JSON.</p>
     * </li>
     * <li><p><code>MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter()</code>: Создается экземпляр объекта <code>MappingJackson2MessageConverter</code>, который является конвертером сообщений для преобразования объектов в JSON и обратно.</p>
     * </li>
     * <li><p><code>converter.setObjectMapper(new ObjectMapper())</code>: Устанавливается объект <code>ObjectMapper</code> в качестве объекта для преобразования объектов Java в JSON и обратно. Этот объект используется конвертером для сериализации и десериализации объектов.</p>
     * </li>
     * <li><p><code>converter.setContentTypeResolver(resolver)</code>: Устанавливается объект <code>resolver</code> в качестве разрешителя MIME-типов для конвертера сообщений. Это позволяет конвертеру определить MIME-тип сообщений на основе содержимого.</p>
     * </li>
     * <li><p><code>messageConverters.add(converter)</code>: Конвертер сообщений добавляется в список конвертеров сообщений приложения.</p>
     * </li>
     * <li><p><code>return false</code>: Метод возвращает <code>false</code>, если не хотим использовать регистр или не хотим регистрировать значение по умолчанию. Поэтому возвращаем <code>false</code>.</p>
     * </li>
     * </ol>
     * <p>Таким образом, данный код настраивает конвертер сообщений для работы с JSON-сообщениями в приложении, что позволяет обмениваться данными в формате JSON между клиентом и сервером.</p>
     *
     * @param messageConverters the converters to configure (initially an empty list)
     * @return
     */
    //    Настраиваем способ преобразования наших сообщений. Или сериализацию/десериализацию
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }
}
