package a.feedbackapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Question {

    private static final String[] stA = {"rating", "selection_single", "selection_multiple", "selection_ranking", "open"};
    private final int type;
    private double min, max, inv;
    private ArrayList<String> sdt;
    private Response resp;
    private String title;
    private ArrayList<Response> arp;

    public class Response {

        private int rating;
        private ArrayList<Integer> selection;
        private String answer;

        public Response() {
            switch (type) {
                case 0: rating = -1; return;
                case 1: selection = new ArrayList<>(sdt.size()); for (String s : sdt) selection.add(0); return;
                case 2: selection = new ArrayList<>(sdt.size()); for (String s : sdt) selection.add(0); return;
                case 3: selection = new ArrayList<>(sdt.size()); for (String s : sdt) selection.add(0); return;
                case 4: answer = ""; return;
            }
        }

        public Response(String data) {parseData(data);}

        public int getRating() {return rating;}

        public void setRating(int rating) {this.rating = rating;}

        public ArrayList<Integer> getSelection() {return selection;}

        public void setSelection(ArrayList<Integer> selection) {this.selection = selection;}

        public String getAnswer() {return answer;}

        public void setAnswer(String answer) {this.answer = answer;}

        public boolean validate() {
            switch (type) {
                case 0: return 0 <= rating && rating < Math.round((max - min) / inv);
                case 1: {
                    if (selection == null) return false;
                    int n = 0;
                    for (int x : selection) n += x;
                    return n == 1;
                }
                case 2: return selection != null;
                case 3: {
                    if (selection == null) return false;
                    ArrayList<Integer> ts = new ArrayList<>(selection);
                    Collections.sort(ts);
                    boolean p = true;
                    for (int i = 0; i < ts.size() && p; i++) p = ts.get(i) == i + 1;
                    return p;
                }
                case 4: return answer != null;
                default: return false;
            }
        }

        public void parseData(String data) {
            switch (type) {
                case 0: rating = Integer.parseInt(data);
                case 1: {
                    selection = new ArrayList<>();
                    String[] t = data.split(",,,");
                    for (String u : t) selection.add(Integer.parseInt(u));
                    return;
                }
                case 2: {
                    selection = new ArrayList<>();
                    String[] t = data.split(",,,");
                    for (String u : t) selection.add(Integer.parseInt(u));
                    return;
                }
                case 3: {
                    selection = new ArrayList<>();
                    String[] t = data.split(",,,");
                    for (String u : t) selection.add(Integer.parseInt(u));
                    return;
                }
                case 4: answer = data; return;
            }
        }

        @Override public String toString() {
            switch (type) {
                case 0: return Integer.toString(rating);
                case 1: {
                    String s = "";
                    for (int i = 0; i < selection.size(); i++) s += (i > 0 ? ",,," : "") + selection.get(i);
                    return s;
                }
                case 2: {
                    String s = "";
                    for (int i = 0; i < selection.size(); i++) s += (i > 0 ? ",,," : "") + selection.get(i);
                    return s;
                }
                case 3: {
                    String s = "";
                    for (int i = 0; i < selection.size(); i++) s += (i > 0 ? ",,," : "") + selection.get(i);
                    return s;
                }
                case 4: return answer;
            }
            return null;
        }
    }

    public Question(int type, String title, String data) {
        this.type = type;
        this.title = title;
        parseData(data);
    }

    private void parseData(String data) {
        switch (type) {
            case 0: {
                String[] t = data.split(",,,");
                min = Double.parseDouble(t[0]);
                max = Double.parseDouble(t[1]);
                inv = Double.parseDouble(t[2]);
                return;
            }
            case 1: {
                sdt = new ArrayList<>(Arrays.asList(data.split(",,,")));
                return;
            }
            case 2: {
                sdt = new ArrayList<>(Arrays.asList(data.split(",,,")));
                return;
            }
            case 3: {
                sdt = new ArrayList<>(Arrays.asList(data.split(",,,")));
                return;
            }
            case 4: return;
        }
    }
    
    public String getData() {
        switch (type) {
            case 0: return min + ",,," + max + ",,," + inv;
            case 1: {
                String s = "";
                for (int i = 0; i < sdt.size(); i++) s += (i > 0 ? ",,," : "") + sdt.get(i);
                return s;
            }
            case 2: {
                String s = "";
                for (int i = 0; i < sdt.size(); i++) s += (i > 0 ? ",,," : "") + sdt.get(i);
                return s;
            }
            case 3: {
                String s = "";
                for (int i = 0; i < sdt.size(); i++) s += (i > 0 ? ",,," : "") + sdt.get(i);
                return s;
            }
            case 4: return "";
        }
        return null;
    }

    public Response getResponse() {return resp;}

    public void setResponse(Response resp) {this.resp = resp;}

    public double getMin() {return min;}
    
    public void setMin(Double min) {this.min = min;}

    public double getMax() {return max;}

    public void setMax(Double max) {this.max = max;}

    public double getInv() {return inv;}

    public void setInv(Double inv) {this.inv = inv;}

    public int getType() {return type;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public ArrayList<String> getOptions() {return sdt;}

    public void setOptions(ArrayList<String> sdt) {this.sdt = sdt;}

    public ArrayList<Response> getResponseSet() {return arp;}

    public void setResponseSet(ArrayList<Response> arp) {this.arp = arp;}

    @Override
    public String toString() {return stA[type] + "'''" + title + "'''" + getData();}

    public static String typeString(int i) {return stA[i];}

    public static int typeNum(String s) {int i; for (i = 0; i < stA.length; i++) if (s.equals(stA[i])) break; return i;}
}
