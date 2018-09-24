package com.tinkai.myroulette;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EditRouletteCautionDialogFragment extends DialogFragment {
    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("注意");
        // 表示する文章設定
        dialogBuilder.setMessage("データがおかしいです。\n確率を確認してください。");

        // OKボタン作成
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }
}
