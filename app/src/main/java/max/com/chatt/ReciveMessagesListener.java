package max.com.chatt;

import org.json.JSONObject;

/**
 * Created by CEC on 03-Jul-18.
 */

public interface ReciveMessagesListener {
    public void Recive();
    public void ReciveOneMessage(Message message , Long ID);
    public void MessageStateChanged(Long ID , int state);
    public void ReciveOneImageMessage(Message Message , Long ID );
    public void ReciveOneFileMessage(Message Message , Long ID );
    public void Image_Downloaded(Long ID , String Path);
    public void Image_Sellected(String Path);
    public void File_Sellected(String Path);
    public void Clear_History();
    public void Block();
    public void UnBlock();
}
