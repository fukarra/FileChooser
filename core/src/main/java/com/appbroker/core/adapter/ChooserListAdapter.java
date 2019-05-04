package com.appbroker.core.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbroker.core.R;
import com.appbroker.core.object.FileObject;

import java.util.ArrayList;

public class ChooserListAdapter extends BaseAdapter {
    private ArrayList<FileObject> fileObjectArrayList;
    private Context context;

    public ChooserListAdapter(Context context, ArrayList<FileObject> fileObjectArrayList) {
        this.context = context;
        this.fileObjectArrayList = fileObjectArrayList;
        Log.d("adapter", String.valueOf(fileObjectArrayList.size()));
    }

    @Override
    public int getCount() {
        return fileObjectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileObjectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.activity_file_chooser_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.hItemImage=convertView.findViewById(R.id.activity_chooser_list_view_item_image);
            viewHolder.hItemTitle=convertView.findViewById(R.id.activity_chooser_list_view_item_title_text);
            viewHolder.hItemInfo=convertView.findViewById(R.id.activity_chooser_list_view_item_info_text);
            viewHolder.hItemLastEdit=convertView.findViewById(R.id.activity_chooser_list_view_item_last_edit_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.hItemImage.setImageDrawable(context.getResources().getDrawable(bringItemDrawable(position)));
        viewHolder.hItemTitle.setText(fileObjectArrayList.get(position).get_title());
        viewHolder.hItemLastEdit.setText(fileObjectArrayList.get(position).get_lastModified());
        viewHolder.hItemInfo.setText(fileObjectArrayList.get(position).get_info());
        return convertView;
    }

    private int bringItemDrawable(int i) {
        final String mimeType=fileObjectArrayList.get(i).get_mimeType();
        final String extension=fileObjectArrayList.get(i).get_extension();
        if (fileObjectArrayList.get(i).is_isFolder()){
            return R.drawable.ic_folder_24px;
        }if (mimeType==null){
            return R.drawable.ic_unknown_24px;
        }else if (fileObjectArrayList.get(i).is_isFolder()){
            return R.drawable.ic_folder_24px;
        }else if (mimeType.startsWith("video/")){
            return R.drawable.ic_video_24px;
        }else if (mimeType.startsWith("audio/")){
            return R.drawable.ic_music_24px;
        }else if (mimeType.startsWith("font/")){
            return R.drawable.ic_font_24px;
        }else if (mimeType.startsWith("image/")){
            return R.drawable.ic_picture_24px;
        }else if (mimeType.startsWith("text/")){
            return R.drawable.ic_text_24px;
        }else if (mimeType.startsWith("model/")){
            return R.drawable.ic_model_24px;
        }else if (mimeType.startsWith("application/pdf")){
            return R.drawable.ic_pdf_24px;
        }else if (mimeType.startsWith("application/vnd.ms-powerpoint")||mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml")){
            return R.drawable.ic_powerpoint_24px;
        }else if (mimeType.startsWith("application/msword")||mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml")||mimeType.startsWith("application/vnd.ms-word")){
            return R.drawable.ic_word_24px;
        }else if (mimeType.startsWith("application/vnd.ms-excel")||mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")){
            return R.drawable.ic_excel_24px;
        }else {
            return R.drawable.ic_unknown_24px;
        }

    }

    class ViewHolder{
        ImageView hItemImage;
        TextView hItemInfo;
        TextView hItemTitle;
        TextView hItemLastEdit;
    }
}
