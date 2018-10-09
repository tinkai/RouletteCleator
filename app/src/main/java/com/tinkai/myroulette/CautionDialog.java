package com.tinkai.myroulette;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Locale;

public class CautionDialog extends DialogFragment {
    private String title;
    private String message;

    public CautionDialog() {}
    public CautionDialog(int id) {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        if (lang.equals("ja")) {
            this.title = "エラー";
            selectMessageJapan(id);
        } else {
            this.title = "Error";
            selectMessageDefault(id);
        }
    }

    private void selectMessageJapan(int id) {
        switch (id) {
            case 0:
                this.message = "ルーレット名が空です。\nルーレット名を確認してください。";
                break;
            case 1:
                this.message = "アイテム名が空です。\nアイテム名を確認してください。";
                break;
            case 2:
                this.message = "アイテム確率が適切ではありません。\nアイテム確率を確認してください。";
                break;
            case 3:
                this.message = "アイテムが2個以下です。\nアイテムを追加してください。";
                break;
        }
    }

    private void selectMessageDefault(int id) {
        switch (id) {
            case 0:
                this.message = "The roulette name is empty.\nPlease check the roulette name.";
                break;
            case 1:
                this.message = "Item name is empty.\nPlease check the item name.";
                break;
            case 2:
                this.message = "Item probability is not appropriate.\nPlease check item probability.";
                break;
            case 3:
                this.message = "There are no more than 2 items.\nPlease add items\n.";
                break;
        }
    }

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle(this.title);
        // 表示する文章設定
        dialogBuilder.setMessage(this.message);

        // OKボタン作成
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // dialogBuilderを返す
        return dialogBuilder.create();
    }
}
