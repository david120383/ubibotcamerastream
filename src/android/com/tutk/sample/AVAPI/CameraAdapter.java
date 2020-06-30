package com.tutk.sample.AVAPI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

//import com.camera.api.AVAPIsClient;
import com.camera.model.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {
    private Context context;
    private List<User> itemList;
    private User mUser;
    private int mintPosition;
    Dialog dialog;
    ClearData clearData;

    public CameraAdapter(Context context, List<User> itemList, ClearData clearData) {
        this.context = context;
        this.itemList = itemList;
        this.clearData = clearData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = Integer.parseInt(v.getTag().toString());
                User user = itemList.get(position);
                String name = user.getName();
                String uid = user.getUID();
                String account = user.getAccount();
                String password = user.getPassword();
                Intent intent = new Intent(context, CamActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("uid", uid);
                intent.putExtra("account", account);
                intent.putExtra("password", password);
                context.startActivity(intent);
            }
        });

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = Integer.parseInt(v.getTag().toString());
                mintPosition = position;
                bottomShow();
            }
        });
        holder.itemView.setTag(position);
        holder.moreInfo.setTag(position);
        holder.name.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView moreInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            moreInfo = itemView.findViewById(R.id.item_set);
        }
    }

    private void bottomShow() {
        dialog = new Dialog(context, R.style.MyDialog);
        // 加载dialog布局view
        View purchase = LayoutInflater.from(context).inflate(R.layout.item_bottom, null);
        TextView delete = purchase.findViewById(R.id.bottom_del);
        TextView update = purchase.findViewById(R.id.bottom_update);
        TextView cancel = purchase.findViewById(R.id.bottom_cancel);

        dialog.setCancelable(true);// 设置外部点击 取消dialog
        Window window = dialog.getWindow(); // 获得窗体对象
        window.setGravity(Gravity.BOTTOM);// 设置窗体的对齐方式
        window.setWindowAnimations(R.style.AnimBottom);// 设置窗体动画
        window.getDecorView().setPadding(0, 0, 0, 0);// 设置窗体的padding
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setContentView(purchase);
        dialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = itemList.get(mintPosition);
                itemList.remove(user);
                clearData.clear(user.getID());
//            Toasty.success(context, "删除成功", Toast.LENGTH_SHORT, true).show();
                dialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = itemList.get(mintPosition);
                String name = user.getName();
                String uid = user.getUID();
                String account = user.getAccount();
                String password = user.getPassword();
                Intent intent = new Intent(context, AddActivity.class);
                intent.putExtra("addflg", "2");
                intent.putExtra("listindexupdate", String.valueOf(mintPosition));
                intent.putExtra("name", name);
                intent.putExtra("uid", uid);
                intent.putExtra("account", account);
                intent.putExtra("password", password);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
