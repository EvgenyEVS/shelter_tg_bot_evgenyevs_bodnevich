package pro.sky.telegrambot.listener;

public final class Constants {

    // Commands
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_SHELTER_INFO = "/shelter_info";
    public static final String COMMAND_ADOPTION_ADVICE = "/adoption_advice";
    public static final String COMMAND_SEND_REPORT = "/send_report";
    public static final String COMMAND_CALL_VOLUNTEER = "/call_volunteer";

    // Actions
    public static final String ACTION_SHELTER_INFO = "shelter_info";
    public static final String ACTION_ADOPTION_ADVICE = "adoption_advice";

    // Menu text
    public static final String MENU_SHELTER_INFO = "🏢 Информация о приюте";
    public static final String MENU_ADOPTION_ADVICE = "📋 Как взять животное";
    public static final String MENU_SEND_REPORT = "📝 Прислать отчёт";
    public static final String MENU_CALL_VOLUNTEER = "👨‍💼 Позвать волонтёра";

    public static final String CHOICE_CATS = "Кошки";
    public static final String CHOICE_DOGS = "Собаки";

    // Callback prefixes
    public static final String CALLBACK_SHELTER_INFO_PREFIX = "shelter_info_";
    public static final String CALLBACK_ADOPTION_ADVICE_PREFIX = "adoption_advice_";
    public static final String CALLBACK_LIST = "list_";
    public static final String CALLBACK_DOCUMENTS = "documents_";
    public static final String CALLBACK_TRANSPORT = "transport_";
    public static final String CALLBACK_HOME_CHILD = "home_child_";
    public static final String CALLBACK_HOME_ADULT = "home_adult_";
    public static final String CALLBACK_HOME_DISABLED = "home_disabled_";
    public static final String CALLBACK_REFUSAL = "refusal_";
    public static final String CALLBACK_COMMUNICATION_DOG = "communication_DOG";
    public static final String CALLBACK_TRAINERS_DOG = "trainers_DOG";
    public static final String CALLBACK_COMMUNICATION_CAT = "communication_CAT";
    public static final String CALLBACK_TRAINERS_CAT = "trainers_CAT";

    // Regex
    public static final String PHONE_REGEX = "^\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}$";

    private Constants() {}
}
