package com.example.timetablin;

import java.util.ArrayList;

class LectureStringify {
    /*
     * Appends each variable for each currently added Lecture to one String. Uses ¬ as a delimiter
     * as it is not often found on Mobile keyboards as a button.
     * @param saveData - ArrayList of type Lecture, contains all currently added entries
     * @return result - String comprised of all variables within saveData
     */
    String arrayToString(ArrayList<Lecture> saveData) {
        //String result = "";
        StringBuilder result = new StringBuilder();
        for(Lecture entr : saveData) {
            result.append(entr.getTitle()).append("¬").append(entr.getDate(false)).append("¬").append(entr.getDate(true)).append("¬")
                    .append(entr.getTime(false)).append("¬").append(entr.getTime(true)).append("¬").append(entr.getCampus()).append("¬")
                    .append(entr.getBuilding()).append("¬").append(entr.getRoom()).append("¬").append(entr.getCategory()).append("¬").append(entr.getNote())
                    .append("¬").append(entr.getId()).append("¬");
        }
    return result.toString();
    }

    /*
     * Converts a string previously created from LectureStringify.arrayToString(ArrayList<Lecture>)
     * into an ArrayList<Lecture>. Uses ¬ as a delimiter as it is not often found on Mobile
     * keyboards as a button.
     * @param saveData - String comprised of all variables of previously added entries
     * @return result - ArrayList of type Lecture containing all previously added (and not deleted) entries
     */
    ArrayList<Lecture> stringToArray(String saveData) {
        ArrayList<Lecture> result = new ArrayList<>();
        String sTitle, sSDate, sEDate, sSTime, sETime, sCampus, sBuilding, sRoom, sCategory, sId, sNote;
        sTitle = sSDate = sEDate = sSTime = sETime = sCampus = sBuilding = sRoom = sCategory = sNote = sId = "";
        int vCount = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < saveData.length(); i++) {
            if (saveData.charAt(i) == '¬') {
                vCount++;
                sb.setLength(0);
                if (vCount == 11) {
                    vCount = 0;
                    int iCategory = Integer.parseInt(sCategory);
                    int iId = Integer.parseInt(sId);
                    result.add(new Lecture(sTitle, sSDate, sEDate, sSTime, sETime, sCampus, sBuilding, sRoom, iCategory, sNote, iId));
                    sTitle = sSDate = sEDate = sSTime = sETime = sCampus = sBuilding = sRoom = sCategory = sNote = sId = "";
                }
            } else {
                switch (vCount) {
                    case 0:
                        sTitle = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 1:
                        sSDate = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 2:
                        sEDate = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 3:
                        sSTime = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 4:
                        sETime = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 5:
                        sCampus = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 6:
                        sBuilding = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 7:
                        sRoom = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 8:
                        sCategory = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 9 :
                        sNote = sb.append(saveData.charAt(i)).toString();
                        break;
                    case 10:
                        sId = sb.append(saveData.charAt(i)).toString();
                        break;
                    default:
                        System.out.println("Parsing Error");
                }
            }
        }
        return result;
    }
}