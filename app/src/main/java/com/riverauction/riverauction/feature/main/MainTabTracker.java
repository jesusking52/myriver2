package com.riverauction.riverauction.feature.main;

import android.content.Context;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.WeakHashMap;

public final class MainTabTracker {

    private MainTabTracker() {
    }

    private static final String TAB_TEACHER = "teacher";
    private static final String TAB_LESSON = "lesson";
    private static final String TAB_MY_LESSON = "my_lesson";
    private static final String TAB_MESSAGE = "message";
    private static final String TAB_PROFILE = "profile";
    private static final String TAB_CONSULT = "consult";
    private static final Map<Integer, String> TABS = Maps.newHashMap();
    static {
        TABS.put(0, TAB_TEACHER);
        TABS.put(1, TAB_LESSON);
        TABS.put(2, TAB_MY_LESSON);
        TABS.put(3, TAB_MESSAGE);
        TABS.put(4, TAB_PROFILE);
        TABS.put(5, TAB_CONSULT);
    }

    private static final WeakHashMap<Integer, MainTabTrackerListener> TAB_VIEWS = new WeakHashMap<>();

    private static int OLD_POSITION;
    private static int NEW_POSITION;

    public static void registerTabCallback(MainTabTrackerListener mainTabTrackerListener, int position, Context context) {
        TAB_VIEWS.put(position, mainTabTrackerListener);

        // HomeView 에서 registerTabCallback() 보다 ViewPager.OnPageChangeListener 의 onPageSelected() 가 먼저 호출되면
        // onEnterTab() 이 안불리므로 registerTabCallback 시의 ViewPager index 를 비교해서 onEnterTab() 을 호출한다.
        if (position == NEW_POSITION) {
            invokeOnEnterTab(position, context);
        }
    }

    public static void unRegisterTabCallback(int position) {
        TAB_VIEWS.remove(position);
    }

    public static void notifyTabSelected(int position, Context context) {
        OLD_POSITION = NEW_POSITION;
        NEW_POSITION = position;
        boolean isTabChanged = OLD_POSITION != NEW_POSITION;

        if (isTabChanged) {
            invokeOnLeaveTab(OLD_POSITION);
            invokeOnEnterTab(NEW_POSITION, context);
        }
    }

    // static 이라서 android back 키 로 Activity 가 내려갔다 올라와도 position 의 위치가 기존 걊으로 설정되어있다.
    // 그래서 MainActivity 에서 ViewPager 에 HomeView, NearbyView 등 4가지 View 를 만들기 전에
    // clearPosition() 을 호출해서 ViewPager 값을 초기화 시킨다.
    public static void clearPosition() {
        OLD_POSITION = 0;
        NEW_POSITION = 0;
        TAB_VIEWS.clear();
    }

    public static int getCurrentPosition() {
        return NEW_POSITION;
    }

    private static void invokeOnLeaveTab(int position) {
        if (!TAB_VIEWS.containsKey(position)) {
            return;
        }

        MainTabTrackerListener mainTabTrackerListener = TAB_VIEWS.get(position);
        mainTabTrackerListener.onLeaveTab();
    }

    private static void invokeOnEnterTab(int position, Context context) {
        if (!TAB_VIEWS.containsKey(position)) {
            return;
        }
        MainTabTrackerListener mainTabTrackerListener = TAB_VIEWS.get(position);
        mainTabTrackerListener.onEnterTab();
    }
}

