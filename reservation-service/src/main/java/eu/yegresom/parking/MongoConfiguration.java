package eu.yegresom.parking;

import com.mongodb.DBObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.hateoas.Link;

import static java.util.Arrays.asList;

/**
 * Created by Sergii Motynga.
 */
@Configuration
public class MongoConfiguration {

    @Bean
    public CustomConversions mongoCustomConversions() {
        return new CustomConversions(asList(new DBObjectToLinkConverter()));
    }

    @ReadingConverter
    public static class DBObjectToLinkConverter implements Converter<DBObject, Link> {
        @Override
        public Link convert(DBObject dbObject) {
            return new Link(dbObject.get("href").toString(), dbObject.get("rel").toString());
        }
    }
}
