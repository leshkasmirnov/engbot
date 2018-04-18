package ru.asmirnov.engbot.db.domain;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
public class Person {

    private Long id;
    private Long extId;
    private PersonStatus status;
    private Long currentDictionaryItemId;

    public Person(Long id, Long extId, PersonStatus status) {
        this.id = id;
        this.extId = extId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public Long getCurrentDictionaryItemId() {
        return currentDictionaryItemId;
    }

    public void setCurrentDictionaryItemId(Long currentDictionaryItemId) {
        this.currentDictionaryItemId = currentDictionaryItemId;
    }


    public static final class PersonBuilder {
        private Long id;
        private Long extId;
        private PersonStatus status;
        private Long currentDictionaryItemId;

        private PersonBuilder() {
        }

        public static PersonBuilder aPerson() {
            return new PersonBuilder();
        }

        public PersonBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PersonBuilder extId(Long extId) {
            this.extId = extId;
            return this;
        }

        public PersonBuilder status(PersonStatus status) {
            this.status = status;
            return this;
        }

        public PersonBuilder currentDictionaryItemId(Long currentDictionaryItemId) {
            this.currentDictionaryItemId = currentDictionaryItemId;
            return this;
        }

        public Person build() {
            Person person = new Person(id, extId, status);
            person.setCurrentDictionaryItemId(currentDictionaryItemId);
            return person;
        }
    }
}
