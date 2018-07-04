package ru.asmirnov.engbot.db.domain;

import java.time.LocalDateTime;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
public class Person {

    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long extId;
    private PersonStatus status;
    private Long currentDictionaryItemId;
    private Boolean statusRequested;

    public Person(Long id, LocalDateTime created, LocalDateTime updated, Long extId, PersonStatus status, Long currentDictionaryItemId, Boolean statusRequested) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.extId = extId;
        this.status = status;
        this.currentDictionaryItemId = currentDictionaryItemId;
        this.statusRequested = statusRequested;
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

    public Boolean getStatusRequested() {
        return statusRequested;
    }

    public void setStatusRequested(Boolean statusRequested) {
        this.statusRequested = statusRequested;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public static final class PersonBuilder {
        private Long id;
        private LocalDateTime created;
        private LocalDateTime updated;
        private Long extId;
        private PersonStatus status;
        private Long currentDictionaryItemId;
        private Boolean statusRequested;

        private PersonBuilder() {
        }

        public static PersonBuilder aPerson() {
            return new PersonBuilder();
        }

        public PersonBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PersonBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public PersonBuilder updated(LocalDateTime updated) {
            this.updated = updated;
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

        public PersonBuilder statusRequested(Boolean statusRequested) {
            this.statusRequested = statusRequested;
            return this;
        }

        public Person build() {
            return new Person(id, created, updated, extId, status, currentDictionaryItemId, statusRequested);
        }
    }
}
