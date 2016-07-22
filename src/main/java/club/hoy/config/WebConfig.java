package club.hoy.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.collect.Lists;

@Configuration
class WebConfig extends WebMvcConfigurationSupport  {
	
	final Charset UTF_8 = Charset.forName("UTF-8");
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(converter());
		
		//当返回的Response为非JSon格式时，对编码仍需进行处理
		StringHttpMessageConverter smc = new StringHttpMessageConverter();
		smc.setWriteAcceptCharset(false);
		smc.setSupportedMediaTypes(Lists.newArrayList(new MediaType("text","html",UTF_8)));
      
		converters.add(smc);
		addDefaultHttpMessageConverters(converters);
    }
	
	@Bean
    MappingJackson2HttpMessageConverter converter() {
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		//使用JSonView处理某个具体请求时Pojo转换成JSon时显示内容
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		//JSon使用下划线风格处理与Pojo驼峰风格的互相转换
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
  
		converter.setSupportedMediaTypes(Lists.newArrayList(
				new MediaType("text","html",UTF_8),new MediaType("text","html",UTF_8),new MediaType("application","json", UTF_8)));
		converter.setObjectMapper(mapper);
        return converter;
    }
  }