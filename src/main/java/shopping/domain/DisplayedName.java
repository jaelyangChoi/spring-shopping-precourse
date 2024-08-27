package shopping.domain;

public class DisplayedName {

    private final String name;

    public DisplayedName(String name, Profanity profanities) {
        if (profanities.contains(name)) {
            throw new IllegalArgumentException("비속어를 포함할 수 없습니다.");
        }

        if (name.length() > 15) {
            throw new IllegalArgumentException("길이가 15을 넘어갑니다.");
        }



        this.name = name;
    }

    public String getName() {
        return name;
    }
}
