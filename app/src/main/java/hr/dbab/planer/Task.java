package hr.dbab.planer;



//creating an annotation of Room and setting the name of the tble
// (by default it would have the name of the class - task)

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    //kreranje member variabli
    //Room automatski generira stupce za ove varijable
    //svaki unos u tablicu ima svoj jednistveni id

    @PrimaryKey(autoGenerate = true)//Room automatski povećava id za svaki novi unos
    private int id;
    private String title;
    private String description;
    private String time;
    private String tag;

    //konstruktor pomoću kojega će se kreirati Task objekti
    //id nije parametar u konstruktoru jer će njega Room sam generirati
    public Task(String title, String description, String time, String tag) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.tag = tag;
    }

    //setter metoda koju će Room koristiti da postavi id na Task objektu
    //id je jedini parametar jer jedino id nije u konstruktoru
    public void setId(int id) {
        this.id = id;
    }

    //getter metode za varijable kako bi Room te vrijednosti mogao spremati u bazu
    // metode su public jer su varijable private
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getTag(){
        return tag;
    }
}

