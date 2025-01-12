package org.supportamhara.fsc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    private Meta meta;
    private int hom;
    private Hwi hwi;
    private String fl;
    private List<Definition> def;
    private History history;
    private List<String> shortdef;

    // Getters and Setters

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public int getHom() {
        return hom;
    }

    public void setHom(int hom) {
        this.hom = hom;
    }

    public Hwi getHwi() {
        return hwi;
    }

    public void setHwi(Hwi hwi) {
        this.hwi = hwi;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public List<Definition> getDef() {
        return def;
    }

    public void setDef(List<Definition> def) {
        this.def = def;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public List<String> getShortdef() {
        return shortdef;
    }

    public void setShortdef(List<String> shortdef) {
        this.shortdef = shortdef;
    }

    // Inner classes for nested objects
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String id;
        private String uuid;
        private String src;
        private String section;
        private List<String> stems;
        private boolean offensive;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public List<String> getStems() {
            return stems;
        }

        public void setStems(List<String> stems) {
            this.stems = stems;
        }

        public boolean isOffensive() {
            return offensive;
        }

        public void setOffensive(boolean offensive) {
            this.offensive = offensive;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hwi {
        private String hw;
        private List<Pronunciation> prs;

        // Getters and Setters
        public String getHw() {
            return hw;
        }

        public void setHw(String hw) {
            this.hw = hw;
        }

        public List<Pronunciation> getPrs() {
            return prs;
        }

        public void setPrs(List<Pronunciation> prs) {
            this.prs = prs;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Pronunciation {
            private String mw;
            private Map<String, String> sound;

            // Getters and Setters
            public String getMw() {
                return mw;
            }

            public void setMw(String mw) {
                this.mw = mw;
            }

            public Map<String, String> getSound() {
                return sound;
            }

            public void setSound(Map<String, String> sound) {
                this.sound = sound;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Definition {
        private List<List<List<Object>>> sseq;

        // Getters and Setters
        public List<List<List<Object>>> getSseq() {
            return sseq;
        }

        public void setSseq(List<List<List<Object>>> sseq) {
            this.sseq = sseq;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class History {
        private String pl;
        private List<List<Map<String, String>>> pt;

        // Getters and Setters
        public String getPl() {
            return pl;
        }

        public void setPl(String pl) {
            this.pl = pl;
        }

        public List<List<Map<String, String>>> getPt() {
            return pt;
        }

        public void setPt(List<List<Map<String, String>>> pt) {
            this.pt = pt;
        }
    }
}

