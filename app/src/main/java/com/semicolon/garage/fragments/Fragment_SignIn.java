package com.semicolon.garage.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.activities.ForgetPasswordActivity;
import com.semicolon.garage.activities.HomeActivity;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignIn extends Fragment{
    private LinearLayout ll_skip;
    private EditText edt_email,edt_password;
    private TextView tv_forget_password;
    private Button btn_signin;
    private ProgressDialog dialog;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signin,container,false);
        initView(view);
        return view;
    }

    public static Fragment_SignIn getInstance()
    {
        return new Fragment_SignIn();
    }

    private void initView(View view) {

        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        tv_forget_password = view.findViewById(R.id.tv_forget_password);
        btn_signin = view.findViewById(R.id.btn_signin);
        ll_skip = view.findViewById(R.id.ll_skip);
        ll_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData() {

        String m_email = edt_email.getText().toString();
        String m_password = edt_password.getText().toString();

        if (!TextUtils.isEmpty(m_email)&&!TextUtils.isEmpty(m_password)&& Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
        {
            edt_email.setError(null);
            edt_password.setError(null);
            Common.CloseKeyBoard(getActivity(),edt_email);
            SignIn(m_email,m_password);
        }else
            {
                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getActivity().getString(R.string.email_req));
                }else
                    {
                        edt_email.setError(null);

                    }


                if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getActivity().getString(R.string.inv_email));
                }else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.pass_req));
                }else
                {
                    edt_password.setError(null);

                }
            }

    }

    private void SignIn(String m_email, String m_pasword) {
        dialog = Common.createProgressDialog(getActivity(),getString(R.string.signin));
        dialog.show();

        Api.getService()
                .SignIn(m_email,m_pasword)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_login()==1)
                            {
                                preferences.create_update_userData(getActivity(),response.body());
                                userSingleTone.setUserModel(response.body());
                                Intent intent = new Intent(getActivity(),HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }else
                                {
                                    Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
