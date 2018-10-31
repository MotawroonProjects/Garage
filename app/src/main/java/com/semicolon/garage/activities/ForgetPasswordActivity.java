package com.semicolon.garage.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView image_back;
    private EditText edt_email;
    private Button btn_reset_password;
    private String lang;
    private ProgressDialog dialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("language");
        image_back = findViewById(R.id.image_back);
        if (lang!=null)
        {
            if (lang.equals("ar"))
            {
                Language.setLocality(this,"ar");
                image_back.setRotation(180f);
            }
        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_email = findViewById(R.id.edt_email);
        btn_reset_password = findViewById(R.id.btn_reset_password);

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

    }

    private void CreateAlertDialog()
    {
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_text_dialog,null);
        Button btnOk = view.findViewById(R.id.openBtn);



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
    }
    private void CheckData() {
        String m_email = edt_email.getText().toString();

        if (!TextUtils.isEmpty(m_email)&& Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
        {
            edt_email.setError(null);
            ResetPassword(m_email);
        }else
            {
                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.email_req));

                }else
                    {
                        edt_email.setError(null);

                    }



                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));

                }else
                {
                    edt_email.setError(null);

                }
            }
    }

    private void ResetPassword(String m_email) {
        dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .resetPassword(m_email)
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_rest()==1)
                            {
                                CreateAlertDialog();
                            }else if (response.body().getSuccess_rest()==0)
                            {
                                Toast.makeText(ForgetPasswordActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(ForgetPasswordActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
