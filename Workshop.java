
public class Workshop extends Events {
    String topic;



    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

   public Workshop(String title, String dateTime, String location, int capacity, boolean status,String topic)
   {
       super(title,dateTime,location,capacity,status);
       this.topic=topic;

   }
}
