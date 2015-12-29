package AangUtil;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import java.util.Arrays;

public class ChatFunc {
    private ClientContext ctx;
    private static ChatFunc ourInstance = new ChatFunc();

    public static ChatFunc getInstance() {
        return ourInstance;
    }

    private static final int CHAT_WIDGET_OTHER_TALKING = 231;
    private static final int CHAT_WIDGET_SELF_TALKING = 217;
    private static final int CHAT_WIDGET_OPTIONS = 219;
    private static final int CHAT_COMPONENT_CONTINUE = 2;

    public void init(ClientContext ctx)
    {
        this.ctx = ctx;
    }

    public boolean chatting(){
        return ctx.widgets.widget(CHAT_WIDGET_OTHER_TALKING).componentCount() > 0
                || ctx.widgets.widget(CHAT_WIDGET_SELF_TALKING).componentCount() > 0
                || ctx.widgets.widget(CHAT_WIDGET_OPTIONS).componentCount() > 0;
    }

    public boolean isChatOpen(int widgetID, int componentID) {
        return ctx.widgets.component(widgetID,componentID).valid();
    }

    public boolean canContinue(){
        return ctx.widgets.component(CHAT_WIDGET_OTHER_TALKING, CHAT_COMPONENT_CONTINUE).valid()
                || ctx.widgets.component(CHAT_WIDGET_SELF_TALKING, CHAT_COMPONENT_CONTINUE).valid();
    }

    public void continueChat(){
        ctx.input.send("{VK_SPACE}");
    }

    public boolean isPendingOption() {
        return ctx.widgets.widget(CHAT_WIDGET_OPTIONS).componentCount() > 0;
    }

    public String getPendingOptionsQuestion() {
        return ctx.widgets.component(CHAT_WIDGET_OPTIONS,0).components()[0].text();
    }

    public Component[] getPendingOptions() {
        Component[] components = ctx.widgets.component(CHAT_WIDGET_OPTIONS,0).components();
        if( components.length == 0 )
            return null;
        return Arrays.copyOfRange(components,1,components.length-2);
    }

    public void chooseOption(int index){
        ctx.input.send("{"+index+"}");
    }

    public boolean pendingInput() {
        return ctx.widgets.component(162, 32).visible();
    }
}
