package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.WebServiceNewsReader;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


/**
 * Created by grusz on 01.04.2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest ({ConfigurationLoader.class,NewsReaderFactory.class,PublishableNews.class})
public class NewsLoaderTest {

    private WebServiceNewsReader webServiceNewsReader;
    private PublishableNews spyPublishableNews;

    @Before
    public void setUp() throws Exception {

        Configuration configuration =  mock(Configuration.class);
        when(configuration.getReaderType()).thenReturn("WS");
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);
        webServiceNewsReader = mock(WebServiceNewsReader.class);
        mockStatic(NewsReaderFactory.class);
        NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(configuration.getReaderType())).thenReturn(webServiceNewsReader);
        mockStatic((PublishableNews.class));
        spyPublishableNews = spy(new PublishableNews());
        when(PublishableNews.create()).thenReturn(spyPublishableNews);
    }

    @Test
    public void prepareForPublish_checkIfPublicNewsAreAddedToPublishable_onePublicNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.NONE));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        List<String> result = (List<String>)Whitebox.getInternalState(spyPublishableNews,"publicContent");
        assertThat(result.size(),is(equalTo(1)));
        assertThat(result.get(0),is(equalTo("bla bla")));

    }

    @Test
    public void prepareForPublish_checkIfAddPublicContentHasBeenCalledOneTime_onePublicNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.NONE));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        Mockito.verify(spyPublishableNews,times(1)).addPublicInfo("bla bla");

    }

    @Test
    @Ignore //Not implemented yet
    public void prepareForPublish_checkIfSubNewsAreAddedToPublishable_oneSubNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.A));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        List<String> result = (List<String>)Whitebox.getInternalState(spyPublishableNews,"subscribentContent");
        assertThat(result.size(),is(equalTo(1)));
        assertThat(result.get(0),is(equalTo("bla bla")));

    }

    @Test
    public void prepareForPublish_checkIfAddPSubContentHasBeenCalledOneTime_oneSubNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.A));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        Mockito.verify(spyPublishableNews,times(1)).addForSubscription("bla bla",SubsciptionType.A);

    }

    @Test
    @Ignore //no implementation for subs yet
    public void prepareForPublish_checkIfPublicAndSubNewsAreAddedToPublishable_onePublicAndOneSubNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("bla bla bla",SubsciptionType.A));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        List<String> result1 = (List<String>)Whitebox.getInternalState(spyPublishableNews,"publicContent");
        List<String> result2 = (List<String>)Whitebox.getInternalState(spyPublishableNews,"subscribentContent");
        assertThat(result1.size(),is(equalTo(1)));
        assertThat(result2.size(),is(equalTo(1)));
        assertThat(result1.get(0),is(equalTo("bla bla")));
        assertThat(result2.get(0),is(equalTo("bla bla bla")));

    }

    @Test
    public void prepareForPublish_checkIfAddPSubContentAndAddPublicContentHasBeenCalledOneTimeEach_onePublicAndSubNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("bla bla bla",SubsciptionType.A));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        spyPublishableNews = newsLoader.loadNews();
        Mockito.verify(spyPublishableNews,times(1)).addPublicInfo("bla bla");
        Mockito.verify(spyPublishableNews,times(1)).addForSubscription("bla bla bla",SubsciptionType.A);

    }
}
