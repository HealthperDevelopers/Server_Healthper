package umc.healthper;

public enum Section {
    BACK("등"),
    LEG("하체"),
    HEAP("엉덩이"),
    CHEST("가슴"),
    SHOULDER("어깨"),
    ABS("복근"),
    TRAPEZIUS("승모근"),
    ARM("팔"),
    CARDIO("유산소"),
    WHOLE("전신");

    private final String value;

    Section(String value){
        this.value = value;

    }

    public String getValue(){
        return value;
    }

}
