package com.semicolon.garage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.models.BankAccountModel;

import java.util.List;

/**
 * Created by elashry on 15/10/2018.
 */

public class BanksAdapter  extends RecyclerView.Adapter<BanksAdapter.Holder>{
    Context context;
    BankAccountModel mmodel;
    List<BankAccountModel> Array;

    public BanksAdapter(Context context, List<BankAccountModel> Array) {
        this.context = context;
        this.Array = Array;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banks_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        mmodel = Array.get(position);


        holder.account_name.setText(mmodel.getAccount_name());
        holder.bank_iban.setText(mmodel.getAccount_IBAN());
        holder.account_bank_name.setText(mmodel.getAccount_bank_name());
        holder.account_num.setText(mmodel.getAccount_number());


    }

    @Override
    public int getItemCount() {
        return Array.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        TextView account_name, bank_iban, account_bank_name,account_num;

        public Holder(View itemView) {
            super(itemView);

            account_name = itemView.findViewById(R.id.txt_account_name);
            bank_iban = itemView.findViewById(R.id.txt_account_IBAN);
            account_bank_name = itemView.findViewById(R.id.txt_account_bank_name);
            account_num = itemView.findViewById(R.id.txt_account_number);



        }



    }

}
