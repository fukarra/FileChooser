package com.appbroker.core.object;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FileObject {
    private Context _context;
    private String _path;
    private String _extension;
    private String _mimeType;
    private String _title;
    private String _lastModified;
    private String _info;
    private boolean _isFolder=false;

    public FileObject(Context context, String path) {
        this._path = path;
        this._context = context;
        getInfo();
    }
    private void getInfo() {
        File file=new File(this._path);
        this._isFolder = file.isDirectory();
        int i = this._path.lastIndexOf('.');
        if (i > 0) {
            this._extension = this._path.substring(i+1);
        }
        this._mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(this._extension);
        this._title = file.getName();
        this._lastModified = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.LONG).format(file.lastModified());
        if (this._isFolder){
            File[] files;
            if ((files=file.listFiles())!=null){
                if (files.length==0||files.length==1){
                    this._info = files.length+" file";
                }else {
                    this._info = files.length+" files";
                }
            }else {
                this._info="?";
            }
        }else {
            this._info = parseSize(file.length());//lenght method returns size in bytes
        }
        Log.d("newFile",_path+"|"+_title+"|"+_extension+"|"+_mimeType+"|"+_isFolder);
    }

    private String parseSize(long length) {
        long preLenght=length;
        String hrSize = "";
        DecimalFormat dec = new DecimalFormat("0.00");
        if ((length=length/1024)>1){
            preLenght=length;
            if ((length=(length/1024)/1024)>1){
                preLenght=length;
                if ((length=((length/1024)/1024)/1024)>1){
                    return dec.format(length).concat(" GB");
                }else {
                    return dec.format(preLenght).concat(" MB");
                }
            }else {
                return dec.format(preLenght).concat(" KB");
            }
        }else {
            return dec.format(preLenght).concat(" Byte");
        }
    }

    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    public String get_extension() {
        return _extension;
    }

    public void set_extension(String _extension) {
        this._extension = _extension;
    }

    public String get_mimeType() {
        return _mimeType;
    }

    public void set_mimeType(String _mimeType) {
        this._mimeType = _mimeType;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_lastModified() {
        return _lastModified;
    }

    public void set_lastModified(String _lastModified) {
        this._lastModified = _lastModified;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public boolean is_isFolder() {
        return _isFolder;
    }

    public void set_isFolder(boolean _isFolder) {
        this._isFolder = _isFolder;
    }
}
