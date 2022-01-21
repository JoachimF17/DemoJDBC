package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Professor {

    private int id;
    private String firstname;
    private String lastname;
    private Section section;
    private int wage;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    @ToString
    public static class Section {
        private int id;
        private String name;
    }
}
