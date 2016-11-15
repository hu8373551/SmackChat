package com.sky.andy.smackchat.manager;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.bean.SearchFriend;
import com.sky.andy.smackchat.bean.UserInfo;
import com.sky.andy.smackchat.listener.StanListener;
import com.sky.andy.smackchat.utils.SystemUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jivesoftware.smack.roster.packet.RosterPacket.ItemType.from;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class SmackManager {
    private static final String HOST = "172.16.1.11";
    //    private static final String HOST = "172.16.4.65";
    //    private static final String HOST = "192.168.2.104";
    private static final int PORT = 5222;

    /**
     *
     */
    public static final String XMPP_CLIENT = "Smack"; //"Smack";


    private static SmackManager mSmackManager;
    /**
     * 连接
     */
    private XMPPTCPConnection connection;


    private SmackManager() {
        this.connection = connect();
    }


    /**
     * 获取操作实例
     *
     * @return
     */
    public static SmackManager getInstance() {
        if (mSmackManager == null) {
            synchronized (SmackManager.class) {
                if (mSmackManager == null) {
                    mSmackManager = new SmackManager();
                }
            }
        }
        return mSmackManager;
    }

    /**
     * 链接服务器
     *
     * @return
     */
    public XMPPTCPConnection connect() {
        try {
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            //服务器IP地址
            configBuilder.setHost(HOST);
            //服务器端口
            configBuilder.setPort(PORT);
            //服务器名称
            configBuilder.setServiceName(HOST);
            //是否开启安全模式
            configBuilder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
            //是否开启压缩
            //configBuilder.setCompressionEnabled(false);
            //开启调试模式
            configBuilder.setDebuggerEnabled(true);
            configBuilder.setSendPresence(true);

            XMPPTCPConnection connection = new XMPPTCPConnection(configBuilder.build());
            connection.connect();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 断开连接，注销
     *
     * @return
     */
    public boolean disconnect() {

        if (!isConnected()) {
            return false;
        }
        connection.disconnect();
        mSmackManager = null;
        return true;
    }

    /**
     * 是否连接成功
     *
     * @return
     */
    public boolean isConnected() {
        if (connection == null) {
            return false;
        }
        if (!connection.isConnected()) {
            try {
                Log.e("hcc", "connection.isConnected()");
                connection.connect();
                return true;
            } catch (Exception e) {
                Log.e("hcc", "e ee-->>>" + e);
                return false;
            }
        }
        return true;
    }


    /**
     * 登陆
     *
     * @param user     用户账号
     * @param password 用户密码
     * @return
     * @throws Exception
     */
    public boolean login(String user, String password) throws Exception {
        if (!isConnected()) {
            return false;
        }
        try {
            connection.login(user, password);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 　用户是否已经登陆
     *
     * @return
     */
    public boolean isLogined() {
        if (!isConnected()) {
            return false;
        }
        return connection.isAuthenticated();
    }


    /**
     * 注册用户信息
     *
     * @param username   账号
     * @param password   账号密码
     * @param attributes 账号其他属性，参考AccountManager.getAccountAttributes()的属性介绍
     * @return
     */
    public boolean registerUser(String username, String password, Map<String, String> attributes) {
        if (!isConnected()) {
            return false;
        }
        try {
            AccountManager.getInstance(connection).createAccount(username, password, attributes);
            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException
                | SmackException.NotConnectedException e) {
            Log.e("hcc", "注册失败", e);
            return false;
        }
    }


    /**
     * 注销
     *
     * @return
     */
    public boolean logout() {
        if (!isConnected()) {
            return false;
        }
        try {
            connection.instantShutdown();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取当前登录用户的所有好友信息
     *
     * @return
     */
    public Set<RosterEntry> getAllFriends() {
        if (isConnected()) {
            return Roster.getInstanceFor(connection).getEntries();
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }


    /**
     * 获取指定账号的好友信息
     *
     * @param user 账号
     * @return
     */
    public RosterEntry getFriend(String user) {
        if (isConnected()) {
            return Roster.getInstanceFor(connection).getEntry(user);
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }


    /**
     * 添加好友
     *
     * @param user      用户账号
     * @param nickName  用户昵称
     * @param groupName 所属组名
     * @return
     */
    public boolean addFriend(String user, String nickName, String groupName) {
        if (isConnected()) {
            try {
                Roster.getInstanceFor(connection).createEntry(user, nickName, new String[]{groupName});
                return true;
            } catch (SmackException.NotLoggedInException | SmackException.NoResponseException
                    | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                return false;
            }
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 过滤监听用户的状态
     *
     * @param context
     */
    public void filterFriend(Context context) {
        AndFilter presence_sub_filter = new AndFilter(new StanzaTypeFilter(Stanza.class));
        connection.addSyncStanzaListener(new StanListener(context), presence_sub_filter);
    }

    /**
     * 获取当前账户昵称
     *
     * @return
     */
    public String getAccountName() {
        if (isConnected()) {
            try {
                return AccountManager.getInstance(connection).getAccountAttribute("name");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 获取当前账户昵称
     *
     * @return
     */
    public String getAccountJid() {
        if (isConnected()) {
            try {
                return SystemUtils.getInstance().spliceStr(connection.getUser());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 获取聊天对象的Fully的jid值
     *
     * @param nickname 用户昵称
     * @return
     */
    public String getChatJidByName(String nickname) {
        RosterEntry friend = SmackManager.getInstance().getFriend(nickname);
        return getChatJidByUser(friend.getUser());
    }

    /**
     * 获取聊天对象的Fully的jid值
     *
     * @param rosterUser 用户账号
     * @return
     */
    public String getChatJidByUser(String rosterUser) {
        if (!isConnected()) {
            throw new NullPointerException("服务器连接失败，请先连接服务器");
        }
        Log.e("hcc", "getChatJidByUser-->>" + connection.getUser() + "\n " + connection.getHost() + " \n " + connection.getStreamId()
                + "\n " + connection.getServiceName() + " \n " + from);
        return rosterUser + "@" + connection.getServiceName();
    }


    /**
     * 创建聊天窗口
     *
     * @param jid 好友的JID
     * @return
     */
    public Chat createChat(String jid) {
        if (isConnected()) {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            return chatManager.createChat(jid);
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 获取聊天对象管理器
     *
     * @return
     */
    public ChatManager getChatManager() {
        if (isConnected()) {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            return chatManager;
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    public List<Message> getRoomChat(String jid) throws Exception {
        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = multiUserChatManager.getMultiUserChat(jid);
        DiscussionHistory discussionHistory = new DiscussionHistory();
        //取某个时间点开始的聊天室消息
        discussionHistory.setSince(new Date(1427090003460L));
        muc.createOrJoin("admin", null, discussionHistory, connection.getPacketReplyTimeout());
        List<Message> messages = new ArrayList<>();
        while (true) {
            //这里超时时间设置为100毫秒
            Message message = muc.nextMessage(5000);
            if (message == null) break;

            System.out.println(message);
            messages.add(message);
        }
        return messages;
    }


    /**
     * 获取文件传输的完全限定Jid
     * The fully qualified jabber ID (i.e. full JID) with resource of the user to send the file to.
     *
     * @param nickname 用户昵称，也就是RosterEntry中的name
     * @return
     */
    public String getFileTransferJid(String nickname) {
        String chatJid = getChatJidByName(nickname);
        return getFileTransferJidChatJid(chatJid);
    }

    /**
     * 获取文件传输的完全限定Jid
     * The fully qualified jabber ID (i.e. full JID) with resource of the user to send the file to.
     *
     * @param chatJid 与好友聊天的限定JID(如：laohu@192.168.0.108)
     * @return
     */
    public String getFileTransferJidChatJid(String chatJid) {
        return chatJid + "/" + XMPP_CLIENT;
    }


    /**
     * 获取发送文件的发送器
     *
     * @param jid 一个完整的jid(如：laohu@192.168.0.108/Smack，后面的Smack应该客户端类型，不加这个会出错)
     * @return
     */
    public OutgoingFileTransfer getSendFileTransfer(String jid) {
        if (isConnected()) {
            return FileTransferManager.getInstanceFor(connection).createOutgoingFileTransfer(jid);
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }


    /**
     * 添加文件接收的监听
     *
     * @param fileTransferListener
     */
    public void addFileTransferListener(FileTransferListener fileTransferListener) {
        if (isConnected()) {
            FileTransferManager.getInstanceFor(connection).addFileTransferListener(fileTransferListener);
            return;
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }


    /**
     * 同意加好友请求
     *
     * @param fromName
     * @param name
     */
    public void acceptFriend(String fromName, String name) {
        Presence presenceRes = new Presence(Presence.Type.subscribed);
        presenceRes.setTo(fromName);
        try {
            connection.sendStanza(presenceRes);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        addFriend(fromName, name, "Friends");
    }


    /**
     * 取消好友添加请求
     *
     * @param fromName
     */
    public void cancelFriend(String fromName) {
        Presence presenceRes = new Presence(Presence.Type.unsubscribe);
        presenceRes.setTo(fromName);
        try {
            connection.sendStanza(presenceRes);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取用户状态
     */
    public void getUserStatus() {
        Roster.getInstanceFor(connection).addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> collection) {
                Log.e("hcc", " presence--11111>>" + collection);
            }

            @Override
            public void entriesUpdated(Collection<String> collection) {
                Log.e("hcc", " presence-->22222>" + collection);
            }

            @Override
            public void entriesDeleted(Collection<String> collection) {
                Log.e("hcc", " presence-->3333>" + collection);
            }

            @Override
            public void presenceChanged(Presence presence) {
                Log.e("hcc", " presence-->4444>" + presence.getFrom());
            }
        });
    }

    /**
     * 获取Roster 对象
     *
     * @return
     */
    public Presence getPresence(String userName) {
        Roster roster = Roster.getInstanceFor(connection);
        Presence presence = roster.getPresence(userName);
        return presence;
    }


    /**
     * 通过文字搜索用户
     *
     * @param searchText
     */
    public ArrayList<SearchFriend> searchUser(String searchText) {
        ArrayList<SearchFriend> list = new ArrayList<>();
        UserSearchManager userSearchManager = new UserSearchManager(connection);
        String searchService = "search." + connection.getServiceName();
        try {
            Form searchForm = userSearchManager.getSearchForm(searchService);
            Form answerForm = searchForm.createAnswerForm();
            // 根据用户名搜索
            answerForm.setAnswer("Username", true);
            // 指定搜索的用户名关键字
            answerForm.setAnswer("search", searchText);
            ReportedData results = userSearchManager.getSearchResults(answerForm, searchService);
            List<ReportedData.Row> rows = results.getRows();
            Log.e("hcc", "results-->>>" + rows);
            for (ReportedData.Row row : rows) {
                String username = row.getValues("Username").get(0);
                String name = row.getValues("Name").get(0);
                String email = row.getValues("Email").get(0);
                SearchFriend searchFriend = new SearchFriend(username, name, email);
                Log.e("hcc", "username-->>>" + username + " name-->>" + name + " email-->>" + email);
                list.add(searchFriend);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


    /**
     * 获取用户头像
     *
     * @param user
     * @return
     */
    public byte[] getUserImage(String user) {
        byte[] userImage = null;
        try {
            VCardManager vCardManager = VCardManager.getInstanceFor(connection);
            VCard vcard = vCardManager.loadVCard(user);
            Log.e("hcc", "vcard-->>" + vcard);
            if (vcard == null || vcard.getAvatar() == null) {
                return null;
            }
//            ByteArrayInputStream bais = new ByteArrayInputStream(
//                    vcard.getAvatar());
//            bitmap = BitmapFactory.decodeStream(bais);
            return vcard.getAvatar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userImage;
    }

    /**
     * 保存用户数据
     *
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        try {
            VCardManager vCardManager = VCardManager.getInstanceFor(connection);
            VCard vcard = vCardManager.loadVCard(userInfo.getBareJid());
            vcard.setNickName(userInfo.getUserName());
            vcard.setAvatar(userInfo.getAvatar());
            vCardManager.saveVCard(vcard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserNickName(UserInfo userInfo) throws Exception {
        Log.e("hcc", "userInfo.getBareJid()-->>" + userInfo.getBareJid() + "\n userName-->>" + userInfo.getUserName());
        VCardManager vCardManager = VCardManager.getInstanceFor(connection);
        VCard vcard = vCardManager.loadVCard(userInfo.getBareJid());
        vcard.setNickName(userInfo.getUserName());
        vcard.setFirstName(userInfo.getUserName());
//            vcard.setAvatar(userInfo.getAvatar());
        vCardManager.saveVCard(vcard);
    }


}
