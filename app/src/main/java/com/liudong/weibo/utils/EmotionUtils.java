package com.liudong.weibo.utils;

import com.liudong.weibo.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liudong on 2016/5/5.
 */
public class EmotionUtils implements Serializable {

    public static Map<String, Integer> emojiMap;

    static {
        emojiMap = new HashMap<String, Integer>();
        emojiMap.put("[爱你]", R.mipmap.d_aini);
        emojiMap.put("[奥特曼]", R.mipmap.d_aoteman);
        emojiMap.put("[拜拜]", R.mipmap.d_baibai);
        emojiMap.put("[悲伤]", R.mipmap.d_beishang);
        emojiMap.put("[鄙视]", R.mipmap.d_bishi);
        emojiMap.put("[闭嘴]", R.mipmap.d_bizui);
        emojiMap.put("[馋嘴]", R.mipmap.d_chanzui);
        emojiMap.put("[吃惊]", R.mipmap.d_chijing);
        emojiMap.put("[打哈气]", R.mipmap.d_dahaqi);
        emojiMap.put("[打脸]", R.mipmap.d_dalian);
        emojiMap.put("[顶]", R.mipmap.d_ding);
        emojiMap.put("[doge]", R.mipmap.d_doge);
        emojiMap.put("[肥皂]", R.mipmap.d_feizao);
        emojiMap.put("[感冒]", R.mipmap.d_ganmao);
        emojiMap.put("[鼓掌]", R.mipmap.d_guzhang);
        emojiMap.put("[哈哈]", R.mipmap.d_haha);
        emojiMap.put("[害羞]", R.mipmap.d_haixiu);
        emojiMap.put("[流汗]", R.mipmap.d_han);
        emojiMap.put("[呵呵]", R.mipmap.d_hehe);
        emojiMap.put("[黑线]", R.mipmap.d_heixian);
        emojiMap.put("[哼]", R.mipmap.d_heng);
        emojiMap.put("[花心]", R.mipmap.d_huaxin);
        emojiMap.put("[挤眼]", R.mipmap.d_jiyan);
        emojiMap.put("[可爱]", R.mipmap.d_keai);
        emojiMap.put("[可怜]", R.mipmap.d_kelian);
        emojiMap.put("[酷]", R.mipmap.d_ku);
        emojiMap.put("[困]", R.mipmap.d_kun);
        emojiMap.put("[懒得理你]", R.mipmap.d_landelini);
        emojiMap.put("[浪]", R.mipmap.d_lang);
        emojiMap.put("[泪]", R.mipmap.d_lei);
        emojiMap.put("[喵]", R.mipmap.d_miao);
        emojiMap.put("[男孩儿]", R.mipmap.d_nanhaier);
        emojiMap.put("[怒]", R.mipmap.d_nu);
        emojiMap.put("[怒骂]", R.mipmap.d_numa);
        emojiMap.put("[女孩儿]", R.mipmap.d_nvhaier);
        emojiMap.put("[钱]", R.mipmap.d_qian);
        emojiMap.put("[亲亲]", R.mipmap.d_qinqin);
        emojiMap.put("[傻眼]", R.mipmap.d_shayan);
        emojiMap.put("[生病]", R.mipmap.d_shengbing);
        emojiMap.put("[神兽]", R.mipmap.d_shenshou);
        emojiMap.put("[失望]", R.mipmap.d_shiwang);
        emojiMap.put("[衰]", R.mipmap.d_shuai);
        emojiMap.put("[睡觉]", R.mipmap.d_shuijiao);
        emojiMap.put("[思考]", R.mipmap.d_sikao);
        emojiMap.put("[太开心]", R.mipmap.d_taikaixin);
        emojiMap.put("[偷笑]", R.mipmap.d_touxiao);
        emojiMap.put("[偷乐]", R.mipmap.d_touxiao);
        emojiMap.put("[兔子]", R.mipmap.d_tuzi);
        emojiMap.put("[挖鼻屎]", R.mipmap.d_wabishi);
        emojiMap.put("[挖鼻]", R.mipmap.d_wabishi);
        emojiMap.put("[委屈]", R.mipmap.d_weiqu);
        emojiMap.put("[笑cry]", R.mipmap.d_xiaoku);
        emojiMap.put("[熊猫]", R.mipmap.d_xiongmao);
        emojiMap.put("[嘻嘻]", R.mipmap.d_xixi);
        emojiMap.put("[嘘]", R.mipmap.d_xu);
        emojiMap.put("[阴险]", R.mipmap.d_yinxian);
        emojiMap.put("[疑问]", R.mipmap.d_yiwen);
        emojiMap.put("[右哼哼]", R.mipmap.d_youhengheng);
        emojiMap.put("[晕]", R.mipmap.d_yun);
        emojiMap.put("[抓狂]", R.mipmap.d_zhuakuang);
        emojiMap.put("[猪头]", R.mipmap.d_zhutou);
        emojiMap.put("[最右]", R.mipmap.d_zuiyou);
        emojiMap.put("[左哼哼]", R.mipmap.d_zuohengheng);
        emojiMap.put("[心]", R.mipmap.l_xin);
        emojiMap.put("[伤心]", R.mipmap.l_shangxin);
        emojiMap.put("[赞]", R.mipmap.lxh_zana);
        emojiMap.put("[good]",R.mipmap.emoji_good);
        emojiMap.put("[得意]",R.mipmap.emoji_deyi);
        emojiMap.put("[加油]",R.mipmap.emoji_jiayou);
        emojiMap.put("[炸弹]",R.mipmap.emoji_zadan);
        emojiMap.put("[啤酒]",R.mipmap.emoji_pijiu);
        emojiMap.put("[悲催]",R.mipmap.lxh_beicui);
        emojiMap.put("[被电]",R.mipmap.lxh_beidian);
        emojiMap.put("[崩溃]",R.mipmap.lxh_bengkui);
        emojiMap.put("[别烦我]",R.mipmap.lxh_biefanwo);
        emojiMap.put("[不好意思]",R.mipmap.lxh_buhaoyisi);
        emojiMap.put("[啤酒]",R.mipmap.emoji_pijiu);
        emojiMap.put("[不想上班]",R.mipmap.lxh_buxiangshangban);
        emojiMap.put("[得意地笑]",R.mipmap.lxh_deyidexiao);
        emojiMap.put("[费劲]",R.mipmap.lxh_feijin);
        emojiMap.put("[好棒]",R.mipmap.lxh_haobang);
        emojiMap.put("[好喜欢]",R.mipmap.lxh_haoxihuan);
        emojiMap.put("[纠结]",R.mipmap.lxh_jiujie);
        emojiMap.put("[围观]",R.mipmap.lxh_quntiweiguan);
        emojiMap.put("[困死了]",R.mipmap.lxh_kunsile);
        emojiMap.put("[雷锋]",R.mipmap.lxh_leifeng);
        emojiMap.put("[玫瑰]",R.mipmap.lxh_meigui);
        emojiMap.put("[噢耶]",R.mipmap.lxh_oye);
        emojiMap.put("[求关注]",R.mipmap.lxh_qiuguanzhu);
        emojiMap.put("[推荐]",R.mipmap.lxh_tuijian);
        emojiMap.put("[想一想]",R.mipmap.lxh_xiangyixiang);
        emojiMap.put("[许愿]",R.mipmap.lxh_xuyuan);

    }

    public static int getImgByName(String imgName) {
        Integer integer = emojiMap.get(imgName);
        return integer == null ? -1 : integer;
    }
}
