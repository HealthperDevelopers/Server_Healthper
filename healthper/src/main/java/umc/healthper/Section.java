package umc.healthper;

import java.util.ArrayList;
import java.util.List;

public enum Section {
    BACK("등",0),
    LEG("하체",1),
    HEAP("엉덩이",2),
    CHEST("가슴",3),
    SHOULDER("어깨",4),
    ABS("복근",5),
    TRAPEZIUS("승모근",6),
    ARM("팔",7),
    CARDIO("유산소",8),
    WHOLE("전신",9);

    private final String value;
    private final int id;
    Section(String value, int id){
        this.value = value;
        this.id = id;
    }

    public String getValue(){
        return value;
    }
    public int getId(){
        return id;
    }
    public static Section getSectionById(int id){
        for(Section s : values()) if(s.getId() == id)return s;
        return null;
    }

    public static List<Section> strToSection(String section_s){
        List<Section> res = new ArrayList<>();
        for(int i=0;i<10;i++) if(section_s.charAt(i) == '1')res.add(getSectionById(i));
        //log.info("{}",res);
        return res;
    }
    public static String listTostr(List<Section> sections){
        int[] arr = new int[10];
        for (Section section : sections) {
            int id = section.getId();
            arr[id]=1;
        }
        String res ="";
        for (int a : arr) {
            if(a == 1) res += '1';
            else res +='0';
        }
        //log.info("listTostr{}",res);
        return res;
    }
}