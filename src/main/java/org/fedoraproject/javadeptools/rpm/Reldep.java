package org.fedoraproject.javadeptools.rpm;

import java.util.Objects;

public class Reldep {
    private String name;
    private int flags;
    private String version;

    public static final int RPMSENSE_LESS = (1 << 1);
    public static final int RPMSENSE_GREATER = (1 << 2);
    public static final int RPMSENSE_EQUAL = (1 << 3);

    public enum Relation {
        le,
        lt,
        ge,
        gt,
        eq,
        ;

        @Override
        public String toString() {
            switch (this) {
            case le: return "<=";
            case lt: return "<";
            case ge: return ">=";
            case gt: return ">";
            case eq: return "=";
            }

            throw new IllegalStateException();
        }
    }

    static Relation[] relations = new Relation[] {
        Relation.le,
        Relation.lt,
        Relation.ge,
        Relation.gt,
        Relation.eq,
        null,
    };

    public Relation getRelation() {
        int index = 5;
        if ((flags & RPMSENSE_LESS) != 0) {
            index -= 4;
        }
        if ((flags & RPMSENSE_GREATER) != 0) {
            index -= 2;
        }
        if ((flags & RPMSENSE_EQUAL) != 0) {
            index -= 1;
        }

        return relations[index];
    }

    Reldep(String name, int flags, String version) {
        this.name = name;
        this.flags = flags;
        this.version = version;
        if (this.version != null && this.version.isEmpty()) {
            this.version = null;
        }
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flags, name, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reldep other) {
            return flags == other.flags && Objects.equals(name, other.name) && Objects.equals(version, other.version);
        }
        return false;
    }

    @Override
    public String toString() {
        String space = ((flags & (RPMSENSE_LESS | RPMSENSE_GREATER| RPMSENSE_EQUAL)) != 0) ? " " : "";
        String result = name;
        result += space;
        if ((flags & RPMSENSE_LESS) != 0) result += "<";
        if ((flags & RPMSENSE_GREATER) != 0) result += ">";
        if ((flags & RPMSENSE_EQUAL) != 0) result += "=";
        result += space;
        result += version;
        return result;
    }

    boolean is(Relation relation, Object other) {
        return version != null && getRelation() == relation && other.equals(version);
    }
}
