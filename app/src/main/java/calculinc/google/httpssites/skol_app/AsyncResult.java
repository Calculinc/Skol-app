package calculinc.google.httpssites.skol_app;

import org.json.JSONObject;


interface AsyncResult
{
    void onResult(JSONObject object);
}