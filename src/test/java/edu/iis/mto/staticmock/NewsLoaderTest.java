package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.WebServiceNewsReader;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


/**
 * Created by grusz on 01.04.2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest ({ConfigurationLoader.class,NewsReaderFactory.class})
public class NewsLoaderTest {

    private WebServiceNewsReader webServiceNewsReader;

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



    }

    @Test
    public void prepareForPublish_checkIPublicNewsAreAddedToPublishable_oneNews() throws Exception {
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("bla bla",SubsciptionType.NONE));
        when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> result = (List<String>)Whitebox.getInternalState(publishableNews,"publicContent");
        assertThat(result.size(),is(equalTo(1)));
        assertThat(result.get(0),is(equalTo("bla bla")));

    }
}
