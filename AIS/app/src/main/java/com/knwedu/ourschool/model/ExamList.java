package com.knwedu.ourschool.model;

import org.json.JSONObject;

/**
 * Created by wel on 19-05-2017.
 */

public class ExamList {
    String is_publish, is_schedule, id, section_id, class_id,
            name_of_exam, exam_term_id, class_name, section_name,
            is_result_publish, term_init_date, attachment, file_name;
    public boolean check;
    public JSONObject object;

    public String getIs_publish() {
        return is_publish;
    }

    public String getIs_schedule() {
        return is_schedule;
    }

    public String getId() {
        return id;
    }

    public String getSection_id() {
        return section_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getName_of_exam() {
        return name_of_exam;
    }

    public String getExam_term_id() {
        return exam_term_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getIs_result_publish() {
        return is_result_publish;
    }

    public String getTerm_init_date() {
        return term_init_date;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getFile_name() {
        return file_name;
    }

    public boolean isCheck() {
        return check;
    }

    public JSONObject getObject() {
        return object;
    }

    public ExamList(JSONObject c) {

        super();
        this.object = c;
        try {
            this.id = c.getString("id").trim();
        } catch (Exception e) {
        }
        try {
            this.is_publish = c.getString("is_publish").trim();
        } catch (Exception e) {
        }
        try {
            this.is_schedule = c.getString("is_schedule").trim();
        } catch (Exception e) {
        }
        try {
            this.section_id = c.getString("section_id").trim();
        } catch (Exception e) {
        }
        try {
            this.class_id = c.getString("class_id").trim();
        } catch (Exception e) {
        }
        try {
            this.name_of_exam = c.getString("name_of_exam").trim();
        } catch (Exception e) {
        }
        try {
            this.exam_term_id = c.getString("exam_term_id").trim();
        } catch (Exception e) {
        }
        try {
            this.class_name = c.getString("class").trim();
        } catch (Exception e) {
        }
        try {
            this.section_name = c.getString("section_name").trim();
        } catch (Exception e) {
        }
        try {
            this.is_result_publish = c.getString("is_result_publish")
                    .trim();
        } catch (Exception e) {
        }
        try {
            this.term_init_date = c.getString("term_init_date").trim();
        } catch (Exception e) {
        }
        try {
            this.attachment = c.getString("attachment").trim();
        } catch (Exception e) {
        }
        try {
            this.file_name = c.getString("file_name").trim();
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        if (object != null) {
            return object.toString();
        }
        return null;
    }
}