package me.pesehr.trustconnection.app;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.lang.reflect.Type;

public class ServiceGenerator {

    public static <S> S createService(Class<S> serviceClass , String url ) {

        RestAdapter.Builder builder;

        builder = new RestAdapter.Builder()
                .setEndpoint(url);

        RestAdapter adapter = builder.build();

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        return adapter.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass,Client client , String url ) {

        RestAdapter.Builder builder;

        builder = new RestAdapter.Builder()
                .setEndpoint(url)
                .setClient(client);

        RestAdapter adapter = builder.build();

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        return adapter.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String accessToken,Client client , String url) {

        RestAdapter.Builder builder;

        builder = new RestAdapter.Builder()
                .setClient(client)
                .setConverter(new Converter() {
                    @Override
                    public String fromBody(TypedInput body, Type type) throws ConversionException {
                        return null;
                    }

                    @Override
                    public TypedOutput toBody(Object object) {
                        return null;
                    }

                });

        builder.setEndpoint(url);

        if (accessToken != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Accept", "application/json");
                    request.addHeader("Authorization", accessToken);
                }
            });
        }

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }



}