package com.example.mylayout;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Fun fun = (Fun) getIntent().getSerializableExtra("fun_data");
        String funName = fun.getName();
        int funImageId = fun.getImageId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView funImageView = (ImageView) findViewById(R.id.fun_image_view);
        TextView funContentText = (TextView) findViewById(R.id.fun_content_text);
        TextView funSuggestionText = (TextView) findViewById(R.id.fun_suggestion_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(funName);
        Glide.with(this).load(funImageId).into(funImageView);

        String funContent = initFunContent(funName);
        funContentText.setText(funContent);

        String funSuggestion = initFunSuggestion(funName);
        funSuggestionText.setText(funSuggestion);
    }

    private String initFunContent(String funName) {
        String content = "";
        switch (funName) {
            case "健身录":
                content = initJianShen();
                break;
            case "测心率":
                content = initXinLv();
                break;
            case "测血糖":
                content = initXueTang();
                break;
            case "量血压":
                content = initXueYa();
                break;
            case "消息站":
                content = initMsg();
                break;
            case "轻松购":
                content = initBuy();
                break;
        }
        StringBuilder funContent = new StringBuilder();
        funContent.append(content);
        return funContent.toString();
    }

    private String initFunSuggestion(String funName) {
        String suggestion = "";
        switch (funName) {
            case "健身录":
                suggestion = sugJianShen();
                break;
            case "测心率":
                suggestion = sugXinLv();
                break;
            case "测血糖":
                suggestion = sugXueTang();
                break;
            case "量血压":
                suggestion = sugXueYa();
                break;
            case "消息站":
                suggestion = sugMsg();
                break;
            case "轻松购":
                suggestion = sugBuy();
                break;
        }
        StringBuilder funSuggestion = new StringBuilder();
        funSuggestion.append(suggestion);
        return funSuggestion.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String initJianShen() {
        return pref.getString("jianshen", "未连接设备，暂无数据!");
    }

    public String initXinLv() {
        return pref.getString("xinlv", "未连接设备，暂无数据!");
    }

    public String initXueTang() {
        return pref.getString("xuetang", "未连接设备，暂无数据!");
    }

    public String initXueYa() {
        return pref.getString("xueya", "未连接设备，暂无数据!");
    }

    public String initMsg() {
        String tmp = pref.getString("updateResult", "还未发送任何信息");
        if(!tmp.equals("还未发送任何信息")){
            String[] sourceStrArray = tmp.split("success");
            return sourceStrArray[1];
        }
        return tmp;
    }

    public String initBuy() {
        return "联系电话 ： 18856017129";
    }

    public String sugJianShen() {
        return "长期坚持跑步 对身体有6个益处\n" +
                "\n" +
                "1、眼睛：坚持长跑的人每天都有1小时候左右的时间眼睛直视远方，这对眼睛是很好的放松休息，如果你家里有学龄的孩子，能让他每天坚持跑步，眼睛近视的几率肯定会降低。\n" +
                "\n" +
                "2、颈部、肩部、脊椎：经常坐在电脑前的人或多或少都会有一些颈椎、肩部的问题，正确的跑步姿势要求背部挺直放松，长期坚持会对颈椎及肩部的不适有很大改善。\n" +
                "\n" +
                "3、心脏：坚持跑步会让你有颗强大的心脏及心血管系统功能。在提高最大摄氧量的同时向身体各个器官输送的氧量大大增加，各个器官的工作质量自然大大提高。另外中长跑会加速血液循环，使冠状动脉有足够的血液供给心肌，从而预防各种心脏病。通过下肢的运动，促使静脉血流回心脏，还预防静脉内血栓形成。\n" +
                "\n" +
                "\n" +
                "4、血液：有了强大的心脏血管系统，跑者的血液质量也好于常人，身体对长期中长跑发生的适应性改变可改善新陈代谢，减低血脂和胆固醇水平。\n" +
                "\n" +
                "5、肺部及呼吸系统：长期进行中长跑锻炼使肺功能变强，增大肺活量——进行规律性的长期长跑可发达肺部呼吸肌，使每次换气量变大，肺功能增强。我个人每年秋天都会有一次季节性鼻炎发作很折磨人，但今年开始跑步后没有复发，不知道有没有关系?\n" +
                "\n" +
                "6、肝脏：我在一次体检的时候，体检医生把实习生叫到跟前说：你们来看看，这才是健康的肝脏，表面血管脉络纹理清晰，现在这样的很少见了。跑步消除脂肪肝，这在很多跑友身上都有了验证，非常有效";
    }

    public String sugXinLv() {
        return "人的心跳次数最好在60-90/分之间。人在大量饮酒、激动、生气、情绪不稳，运动以及感染、高热时都会造成心跳加快。长期坚持适度运动可以使心跳变慢。\n" +
                "　　剧烈运动，大量饮酒，常生气、发怒等都对人体有害，所以要保持一颗平静的心很重要。坚持适度运动，可以使心脏得到锻炼又不至于心跳过快，决定寿命的长短主要还靠遗传基因。\n" +
                "　　心率越慢寿命越长\n" +
                "　　科学家们进一步证实，在所有哺乳动物中都可发现这一规律。有趣的是，仓鼠每分钟心跳约500-600次，是鲸的20-30倍，然而它的体重只是鲸的50万之一。\n" +
                "　　人们发现，一种叫格拉帕哥斯的乌龟寿命可长达177年，它的每分钟心跳仅为6次，一生心脏共跳动约5.6亿次。令人惊奇的是，所有哺乳动物（人除外）一生的心跳次数基本一样，大约是7.3亿次左右。\n" +
                "　　每种动物大小虽然不同，但心脏重量与体重的比例大致相同，都是体重的0.5%-0.6%。\n" +
                "　　目前，这些现象的确切原因尚未完全阐明，但人们提出了一种解释：心率由机体能量代谢需求决定，遵守生物物理学规律，机体能量耗尽，生命也就终结，而心率正是反映机体能量代谢的有效指标。\n" +
                "　　心脏是人体非常重要的器官之一，心脏的使用寿命决定了人体的寿命，因此，在生活中，我们要好好的保护我们的心脏，希望能够帮助大家哦！";
    }

    public String sugXueTang() {
        return "高血糖是人们在日常的生活中非常常见哦，特别是一些中老年人大部分都是高血糖患者，而高血糖的危害是非常的大的，有时甚至会危及生命，而降血糖的方法也是非常的多的，有饮食、运动等待，今天小编要为大家介绍的则是一些可以降血糖的蔬菜。\n" +
                "\n" +
                "　　降血糖的蔬菜\n" +
                "\n" +
                "　　蔬菜是我们生活的必需品，它的功效也是比较多的，有的蔬菜就可以降血糖，下面小编就为大家详细的介绍一下吧!\n" +
                "\n" +
                "　　空心菜\n" +
                "\n" +
                "　　各种营养成分含量比西红柿高出许多倍。如维生素A高6倍，维生素B2高7倍，维生素C高两倍，蛋白质高4倍，钙高12倍。同时，其丰富的纤维素和胰岛素样成分可治疗糖尿病。\n" +
                "\n" +
                "　　洋葱\n" +
                "\n" +
                "　　含维生素A、B1、B6、C等，并有杀菌作用，能抑制高脂餐引起的血浆胆固醇升高，还含有与降糖药物甲磺丁脲相似的有机物，适于糖尿病合并动脉硬化者食用。\n" +
                "苦瓜\n" +
                "\n" +
                "　　含有类似胰岛素的物质，可以降血糖。还含有大量的纤维素，可以延缓小肠对糖的吸收而使血糖下降。\n" +
                "\n" +
                "　　南瓜\n" +
                "\n" +
                "　　含有较多的铬、镍微量元素，可降血糖。南瓜又是高纤维食品，可以延缓小肠对糖的吸收，减轻胰岛细胞的负担。\n" +
                "\n" +
                "　　黄瓜\n" +
                "\n" +
                "　　含维生素C，可改善糖代谢，降血糖。黄瓜所含的葡萄糖、米糖和木糖不参与通常的糖代谢，对糖尿病有较好疗效。\n" +
                "\n" +
                "　　胡萝卜\n" +
                "\n" +
                "　　健脾化滞、养肾壮阳，含胡萝卜素、维生素等多种成分，以及一种无定形黄色成分，有明显的降血糖作用。";
    }

    public String sugXueYa() {
        return "养生要点：心态平和，避免激动。\n" +
                "\n" +
                "　　清晨6～9时\n" +
                "\n" +
                "　　清晨6～9时，人们刚从梦乡醒来，体内元气尚未完全恢复，体温也较低，血流缓慢，如果突然起身或剧烈运动，会引起血压不稳或脑供血不足而出现眩晕。另外，这段时间体内水分缺乏，血液浓缩，如果不及时补充水分，长期下去可形成血栓，从而导致缺血性脑中风的发生。\n" +
                "\n" +
                "　　养生要点：晨起后适当喝些温开水、牛奶，不要做剧烈运动。\n" +
                "\n" +
                "　　餐后1小时\n" +
                "\n" +
                "　　医学观察发现，不当的饮食习惯，如暴饮暴食，会使血压产生明显波动，幅度可达20～30毫米汞柱，易导致心绞痛、心肌梗塞等疾病的发生。因此，高血压患者忌暴饮暴食，饭后不宜立即进行活动。\n" +
                "\n" +
                "　　养生要点：吃七八分饱，别贪多，别求快。\n" +
                "\n" +
                "　　屏气排便时\n" +
                "\n" +
                "　　下蹲排便时，由于腹压加大，可使血压升高，特别是在便秘时，屏气用力排便，会促进全身肌肉、血管收缩，致使较多的血液充盈颅内血管，从而导致脑出血的发生。\n" +
                "\n" +
                "　　养生要点：平时多喝白开水，多吃蔬菜水果，保持大便通畅。若遇便秘，切忌用力排便，可借助开塞露。\n" +
                "\n" +
                "　　洗浴时\n" +
                "\n" +
                "　　洗澡时，高血压患者极易发生心脑血管意外，这主要是由于在热水或冷水刺激下，血管功能发生波动所致。\n" +
                "\n" +
                "　　养生要点：洗浴时水温不能过高，时间不宜过长。";
    }

    public String sugMsg() {
        return "接受服务器反馈信息";
    }

    public String sugBuy() {
        return "联系医生，预购药";
    }
}
