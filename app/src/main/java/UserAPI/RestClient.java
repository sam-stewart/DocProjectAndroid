package UserAPI;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient
{
    private static UserApi REST_CLIENT;
    private static String ROOT = "http://meat.stewpot.nz:3000/";

    private RestClient()
    {
        // Custom deserializer to convert UTC time to device local time zone
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
                {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try
                        {
                            return format.parse(json.getAsString());
                        }
                        catch (ParseException e)
                        {
                            return null;
                        }
                    }
                })
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(UserApi.class);
    }

    public static UserApi get()
    {
        if(REST_CLIENT == null)
        {
            if(REST_CLIENT == null)
                new RestClient();
        }
        return REST_CLIENT;
    }
}
