package keni.paritet.Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Keni on 09.11.2016.
 */

public class Config
{
    // Add
    public static final String TASK_ADD_URL = "https://www.example.com/taskAdd.php";
    public static final String REPORT_ADD_URL = "https://www.example.com/reportAdd.php";
    public static final String COMMENT_ADD_URL = "https://www.example.com/commentAdd.php";

    // Update
    public static final String COMMENT_UPDATE_URL = "https://www.example.com/commentUpdate.php";
    public static final String REPORT_UPDATE_URL = "https://www.example.com/reportUpdate.php";

    // Gets
    public static final String GET_PARAMS_URL = "https://www.example.com/getParams.php?login=";
    public static final String GET_TASKS_URL = "https://www.example.com/getTasks.php?user_id=";
    public static final String GET_TASKS_FILTERED = "https://www.example.com/getFiltered.php";
    public static final String GET_TASK_INFO_URL = "https://www.example.com/getTaskInfo.php?app_id=";
    public static final String GET_USERS_URL = "https://www.example.com/getUsers.php";
    public static final String GET_SOLUTIONS_URL = "https://www.example.com/getSolutions.php";
    public static final String GET_PRIORITY_URL = "https://www.example.com/getPriority.php";
    public static final String GET_STATUS_URL = "https://www.example.com/getStatus.php";
    public static final String GET_OBJECTS_URL = "https://www.example.com/getObjects.php";

    // Other
    public static final String CHECK_IMEI_URL = "https://www.example.com/checkIMEI.php";
    public static final String LOGIN_URL = "https://www.example.com/auth.php";
    public static final String START_TASK_URL = "https://www.example.com/startTask.php";
    public static final String FINISH_TASK_URL = "https://www.example.com/finishTask.php";
    public static final String TRANSFER_TASK_URL = "https://www.example.com/transferTask.php";
    public static final String REPORT_DELETE_URL = "https://www.example.com/reportDelete.php";
    public static final String RENEW_TASK_URL = "https://www.example.com/renewTask.php";

    public static final String IMEI_SUCCESS = "IMEI checked";

    // Переменные авторизированого пользователя
    public static final String LOGIN_SUCCESS = "success";
    public static final String SHARED_PREF_NAME = "app";
    public static final String LOGGED_SHARED_PREF = "logged";
    public static final String auth_user_id = "id";
    public static final String auth_full_name = "full_name";
    public static final String auth_user_avatar = "avatar";

    public static final String JSON_ARRAY = "result";

    public static final String TASK_APP_ID = "app_id";
    public static final String TASK_CREATOR_ID = "creator_id";
    public static final String TASK_OBJ = "obj_id";
    public static final String TASK_CREATE_DATE = "create_dt";
    public static final String TASK_EXPIRE_DATE = "expire_dt";
    public static final String TASK_REASON = "reason";
    public static final String TASK_PERFORMER = "performer";
    public static final String TASK_PERFORMER_ID = "performer_id";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_STATUS = "status";

    public static final String REPORT_ID = "report_id";
    public static final String REPORT_TYPE = "type";
    public static final String REPORT_USER = "user_id";
    public static final String REPORT_COMMENT = "comment";
    public static final String REPORT_CREATE_DT = "create_dt";
    public static final String REPORT_DT0 = "dt0";
    public static final String REPORT_DT1 = "dt1";
    public static final String REPORT_DT0_DATE = "dt0Date";
    public static final String REPORT_DT1_DATE = "dt1Date";
    public static final String REPORT_DT0_TIME = "dt0Time";
    public static final String REPORT_DT1_TIME = "dt1Time";
    public static final String REPORT_SOLUTION = "solution";
    public static final String REPORT_SOLUTION_ID = "solution_id";
    public static final String REPORT_SOLUTION_IMG = "solution_img";
    public static final String REPORT_ACT = "act";
    public static final String REPORT_AVATAR = "avatar";
    public static final String REPORT_TO_USER_ID = "to_user_id";
    public static final String REPORT_STATUS = "report_status";

    public static final String SHARED_PREF_FILTER = "filtered";
    public static final String FILTERED = "filtered";
    public static final String FILTERED_PERFORMER = "performer_id";
    public static final String FILTERED_PRIORITY = "priority";
    public static final String FILTERED_STATUS = "status";
    public static final String FILTERED_OBJECT = "object_id";
    public static final String FILTERED_DT0 = "dt0";
    public static final String FILTERED_DT1 = "dt1";
    public static final String FILTERED_SORT = "sort";

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USER = "user";
    public static final String TAG_USER_FULL_NAME = "full_name";
    public static final String TAG_USER_AVATAR = "avatar";
    public static final ArrayList<HashMap<String, String>> users = new ArrayList<>();
    public static final ArrayList<String> performers = new ArrayList<>();

    public static final String TAG_PRIORITY_ID = "priority_id";
    public static final String TAG_PRIORITY_NAME = "priority_name";
    public static final ArrayList<HashMap<String, String>> priorityList = new ArrayList<>();
    public static final ArrayList<String> priority = new ArrayList<>();

    public static final String TAG_STATUS_ID = "status_id";
    public static final String TAG_STATUS_NAME = "status_name";
    public static final ArrayList<HashMap<String, String>> statusList = new ArrayList<>();
    public static final ArrayList<String> status = new ArrayList<>();

    public static final String TAG_OBJECT_ID = "object_id";
    public static final String TAG_OBJECT_NAME = "object_name";
    public static final ArrayList<HashMap<String, String>> objectsList = new ArrayList<>();
    public static final ArrayList<String> objects = new ArrayList<>();

    public static final String TAG_SOLUTION_ID = "sid";
    public static final String TAG_SOLUTION_NAME = "solution";
    public static final ArrayList<HashMap<String, String>> solution = new ArrayList<>();

    public static final String IN_WORK_STATUS = "3";
    public static final String ACCEPT_STATUS = "5";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
