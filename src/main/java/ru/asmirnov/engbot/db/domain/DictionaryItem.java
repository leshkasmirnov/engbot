package ru.asmirnov.engbot.db.domain;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
public class DictionaryItem {

    private Long id;
    private String original;
    private String translate;
    private Long userId;
    private DictionaryItemMark mark;

    public DictionaryItem(Long id, String original, String translate, Long userId, DictionaryItemMark mark) {
        this.id = id;
        this.original = original;
        this.translate = translate;
        this.userId = userId;
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DictionaryItemMark getMark() {
        return mark;
    }

    public void setMark(DictionaryItemMark mark) {
        this.mark = mark;
    }


    public static final class DictionaryItemBuilder {
        private Long id;
        private String original;
        private String translate;
        private Long userId;
        private DictionaryItemMark mark;

        private DictionaryItemBuilder() {
        }

        public static DictionaryItemBuilder aDictionaryItem() {
            return new DictionaryItemBuilder();
        }

        public DictionaryItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DictionaryItemBuilder original(String original) {
            this.original = original;
            return this;
        }

        public DictionaryItemBuilder translate(String translate) {
            this.translate = translate;
            return this;
        }

        public DictionaryItemBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public DictionaryItemBuilder mark(DictionaryItemMark mark) {
            this.mark = mark;
            return this;
        }

        public DictionaryItem build() {
            return new DictionaryItem(id, original, translate, userId, mark);
        }
    }
}
