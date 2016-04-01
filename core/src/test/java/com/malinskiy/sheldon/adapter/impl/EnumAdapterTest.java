package com.malinskiy.sheldon.adapter.impl;

import com.malinskiy.sheldon.IGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.Observer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnumAdapterTest {

    @Mock IGateway gateway;
    @Mock Observer<NUMBER> observer;
    private com.malinskiy.sheldon.adapter.impl.EnumAdapter<NUMBER> adapter;

    public enum NUMBER {
        ONE, TWO
    }

    @Before
    public void init() {
        adapter = new com.malinskiy.sheldon.adapter.impl.EnumAdapter<>(NUMBER.class);
    }

    @Test
    public void testObserve() throws Exception {
        when(gateway.observeString("testKey", NUMBER.ONE.name()))
                .thenReturn(Observable.just(NUMBER.TWO.name()));

        Observable<NUMBER> observable = adapter.observe("testKey", NUMBER.ONE, gateway);
        observable.subscribe(observer);

        verify(gateway).observeString("testKey", NUMBER.ONE.name());

        verify(observer).onNext(NUMBER.TWO);
        verify(observer).onCompleted();
        verify(observer, never()).onError(any(Throwable.class));
    }

    @Test
    public void testPut() throws Exception {
        adapter.put("testKey", NUMBER.TWO, gateway);

        verify(gateway).putString("testKey", NUMBER.TWO.name());
    }
}