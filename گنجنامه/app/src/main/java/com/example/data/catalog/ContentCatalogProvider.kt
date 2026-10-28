package com.example.data.catalog

import com.example.data.model.GameStage
import com.example.data.model.KnowledgeCard
import com.example.data.model.QuizOption
import com.example.data.model.QuizQuestion
import com.example.data.model.RealmInfo
import com.example.data.model.VisualRiddle

object ContentCatalogProvider {

    val realms: List<RealmInfo> = listOf(
        RealmInfo(
            id = "shahnameh",
            title = "باغ سخن",
            subtitle = "ادبیات و شاهنامه فردوسی",
            description = "آغاز سفر در دیار اسطوره‌ها، سیمرغ پرورنده زال، رستم دستان و نبردهای حماسی ایران زمین.",
            requiredStars = 0,
            iconSymbol = "📜",
            accentColorHex = 0xFFD4AF37,
            stagesCount = 5
        ),
        RealmInfo(
            id = "history",
            title = "دیار کهن",
            subtitle = "تاریخ و فرهنگ باستان",
            description = "کشف منشور کوروش بزرگ، کاخ‌های تخت جمشید و استواری پاسارگاد در گذر سده‌ها.",
            requiredStars = 12,
            iconSymbol = "🏛️",
            accentColorHex = 0xFF00A896,
            stagesCount = 5
        ),
        RealmInfo(
            id = "geography",
            title = "شگفتی‌های خاور",
            subtitle = "جغرافیا و جاذبه‌های دیار",
            description = "از ستیغ مهربان دماوند و کویر لوت تا خلیج نیلگون فارس و دشت‌های سرسبز هیرکانی.",
            requiredStars = 24,
            iconSymbol = "🏔️",
            accentColorHex = 0xFF028090,
            stagesCount = 5
        ),
        RealmInfo(
            id = "science",
            title = "سرای حکمت",
            subtitle = "علوم و فرزانگان ایرانی",
            description = "نگاهی به اندیشه خیام، بوعلی سینا، رازی، خوارزمی و پایه‌گذاری علوم پزشکی و ریاضی.",
            requiredStars = 36,
            iconSymbol = "🔬",
            accentColorHex = 0xFFC84B31,
            stagesCount = 5
        ),
        RealmInfo(
            id = "logic",
            title = "منطق و شطرنج",
            subtitle = "ریاضیات، شطرنج و پازل‌ها",
            description = "هوش‌ورزی بزرگمهر بختگان، اختراع شطرنج و تخته نرد، و معماهای محاسباتی هوشمندانه.",
            requiredStars = 48,
            iconSymbol = "♟️",
            accentColorHex = 0xFF6366F1,
            stagesCount = 5
        ),
        RealmInfo(
            id = "architecture",
            title = "خشت و فیروزه",
            subtitle = "هنر، معماری و اسلیمی",
            description = "آسمان فیروزه‌ای نقش جهان، کتیبه‌های عالی‌قاپو و شاهکارهای کاشی‌کاری و ارسی‌ها.",
            requiredStars = 60,
            iconSymbol = "🕌",
            accentColorHex = 0xFF10B981,
            stagesCount = 5
        ),
        RealmInfo(
            id = "music",
            title = "نغمه‌های باربد",
            subtitle = "موسیقی سنتی و نغمه‌ها",
            description = "سازهای اصیل ایرانی، سرودهای باربد و نکیسا و دستگاه‌های ماهور، همایون و شور.",
            requiredStars = 72,
            iconSymbol = "🪕",
            accentColorHex = 0xFFEC4899,
            stagesCount = 5
        ),
        RealmInfo(
            id = "philosophy",
            title = "آیین اندیشه",
            subtitle = "حکمت اشراق و فلسفه",
            description = "سیر در نور سهروردی، حکمت متعالیه ملاصدرا و اندیشه‌های سترگ فارابی و ابن‌رشد.",
            requiredStars = 84,
            iconSymbol = "✨",
            accentColorHex = 0xFFF59E0B,
            stagesCount = 5
        )
    )

    val stages: Map<Int, GameStage> = mapOf(
        1 to GameStage(
            stageNumber = 1,
            realmId = "shahnameh",
            stageTitle = "پرنده اسطوره‌ای البرز",
            targetWords = listOf("سیمرغ", "مرغ", "سیر", "رسم", "مسیر"),
            bonusWords = listOf("گرم", "سرم", "مری", "میس", "ریسم"),
            letters = listOf('س', 'ی', 'م', 'ر', 'غ'),
            knowledgeCard = KnowledgeCard(
                id = 101,
                title = "سیمرغ در شاهنامه",
                realmTitle = "باغ سخن",
                summary = "سیمرغ پرنده‌ای خردمند و درمان‌بخش در ستیغ البرز است که زال را همچون مادری مهربان در آشیانه خویش پروراند.",
                wisdomQuote = "یکی پرّ من بر ترنجش بمال / بیابی به فرّم درخشان جلال",
                era = "دوران اساطیری ایران",
                iconSymbol = "🦅"
            ),
            visualRiddle = VisualRiddle(
                id = 201,
                title = "پرورنده زال سپیدموی",
                clueText = "پرنده‌ای بلندپرواز در کوه قاف که با پر شفا‌بخش خود رستم دستان را در نبرد رهانید.",
                answerWord = "سیمرغ",
                visualType = "SIMURGH",
                scrambledLetters = listOf('س', 'ی', 'م', 'ر', 'غ', 'ن', 'د', 'ا')
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 301,
                    question = "پرورنده زال در شاهنامه فردوسی کدام پرنده افسانه‌ای بود؟",
                    options = listOf(
                        QuizOption("سیمرغ", true),
                        QuizOption("ققنوس", false),
                        QuizOption("هما", false),
                        QuizOption("شاهین", false)
                    ),
                    explanation = "سیمرغ زال را که به دلیل سپیدی موی در کوه رها شده بود پروراند و به او دانش و خرد بخشید."
                ),
                QuizQuestion(
                    id = 302,
                    question = "سیمرغ در کدام کوه اسطوره‌ای بلند آشیان داشت؟",
                    options = listOf(
                        QuizOption("البرز کوه", true),
                        QuizOption("کوه سهند", false),
                        QuizOption("الوند", false),
                        QuizOption("بینالود", false)
                    ),
                    explanation = "در شاهنامه آمده که آشیانه سیمرغ بر قله کوه سر به فلک کشیده البرز جای داشت."
                ),
                QuizQuestion(
                    id = 303,
                    question = "رستم در کدام نبرد تن‌به‌تن از پر سیمرغ برای چاره‌جویی یاری گرفت؟",
                    options = listOf(
                        QuizOption("نبرد با اسفندیار", true),
                        QuizOption("نبرد با سهراب", false),
                        QuizOption("جنگ با افراسیاب", false),
                        QuizOption("هفت‌خوان مازندران", false)
                    ),
                    explanation = "سیمرغ به رستم درخت گز را نشان داد تا پیکان تیر گزین را برای مقابله با رویین‌تنی اسفندیار بسازد."
                )
            ),
            rewardCoins = 50
        ),

        2 to GameStage(
            stageNumber = 2,
            realmId = "shahnameh",
            stageTitle = "پهلوان پیلتن سیستان",
            targetWords = listOf("رستم", "ترس", "سمت", "مست", "سرم"),
            bonusWords = listOf("ترمس", "ست", "تم", "رم"),
            letters = listOf('ر', 'س', 'ت', 'م'),
            knowledgeCard = KnowledgeCard(
                id = 102,
                title = "رستم دستان",
                realmTitle = "باغ سخن",
                summary = "رستم بزرگ‌ترین جهان‌پهلوان تاریخ اسطوره‌ای ایران، فرزند زال و رودابه و حامی استواری مرزهای ایران‌زمین است.",
                wisdomQuote = "چنین گفت رستم به توران سپاه / منم رستم زال با گرز و ماه",
                era = "دوره پهلوانی شاهنامه",
                iconSymbol = "⚔️"
            ),
            visualRiddle = VisualRiddle(
                id = 202,
                title = "خداوندگار رخش و گرز سام",
                clueText = "پهلوان نامدار سیستان که هفت‌خوان مازندران را برای نجات شاه ایران درنوردید.",
                answerWord = "رستم",
                visualType = "ROSTAM",
                scrambledLetters = listOf('ر', 'س', 'ت', 'م', 'د', 'ل', 'ی', 'ر')
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 304,
                    question = "اسب نامدار، تیزپا و وفادار رستم چه نام داشت؟",
                    options = listOf(
                        QuizOption("رخش", true),
                        QuizOption("شبدیز", false),
                        QuizOption("گلگون", false),
                        QuizOption("بور", false)
                    ),
                    explanation = "رخش اسبی تنومند و هوشیار بود که رستم از میان هزاران اسب زابلستان برگزید."
                ),
                QuizQuestion(
                    id = 305,
                    question = "رستم برای نجات کیکاووس از اسارت دیوان از چند خوان سهمگین گذشت؟",
                    options = listOf(
                        QuizOption("هفت‌خوان", true),
                        QuizOption("پنج‌خوان", false),
                        QuizOption("ده‌خوان", false),
                        QuizOption("سه خوان", false)
                    ),
                    explanation = "هفت‌خوان رستم شامل شیر بیشه، تشنگی، اژدها، زن جادو، اولاد، ارژنگ دیو و دیو سپید بود."
                ),
                QuizQuestion(
                    id = 306,
                    question = "جامه جنگی معروف رستم که از پوست پلنگ ساخته شده بود چه نام داشت؟",
                    options = listOf(
                        QuizOption("ببر بیان", true),
                        QuizOption("جوشن پولاد", false),
                        QuizOption("خفتان زری", false),
                        QuizOption("قبا", false)
                    ),
                    explanation = "ببر بیان جامه‌ای رویین‌وار بود که نه آتش بر آن کارگر بود و نه آب و شمشیر."
                )
            ),
            rewardCoins = 60
        ),

        3 to GameStage(
            stageNumber = 3,
            realmId = "shahnameh",
            stageTitle = "بام سربلند دماوند",
            targetWords = listOf("دماوند", "دام", "دوان", "نماد", "ماد", "مداد"),
            bonusWords = listOf("نمد", "دود", "دم", "وام", "امود"),
            letters = listOf('د', 'م', 'ا', 'و', 'ن', 'د'),
            knowledgeCard = KnowledgeCard(
                id = 103,
                title = "دماوند در اساطیر",
                realmTitle = "باغ سخن",
                summary = "دماوند، نگین بلند البرز، نماد پایداری و مقاومت ایرانیان است؛ جایی که ضحاک ستمگر به زنجیر فریدون در بند کشیده شد.",
                wisdomQuote = "ای دیو سپید پای در بند / ای گنبد گیتی ای دماوند",
                era = "اساطیر کهن",
                iconSymbol = "🏔️"
            ),
            visualRiddle = VisualRiddle(
                id = 203,
                title = "زندان ماردوش تاریخ",
                clueText = "بلندترین ستیغ آتشفشانی ایران و گنبد سرفراز گیتی در البرز مرکزی.",
                answerWord = "دماوند",
                visualType = "DAMAVAND",
                scrambledLetters = listOf('د', 'م', 'ا', 'و', 'ن', 'د', 'ک', 'و')
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 307,
                    question = "کدام پادشاه پاک‌نهاد پیشدادی ضحاک ستمگر را در دماوند به بند کشید؟",
                    options = listOf(
                        QuizOption("فریدون", true),
                        QuizOption("جمشید", false),
                        QuizOption("منوچهر", false),
                        QuizOption("کیقباد", false)
                    ),
                    explanation = "فریدون به راهنمایی سروش اهورایی، ضحاک ماردوش را در غار دماوند به بند جاودان کشید."
                ),
                QuizQuestion(
                    id = 308,
                    question = "کدام قهرمان اسطوره‌ای تیر پرآوازه مرزبندی ایران را از فراز دماوند رها کرد؟",
                    options = listOf(
                        QuizOption("آرش کمانگیر", true),
                        QuizOption("رستم", false),
                        QuizOption("گیو", false),
                        QuizOption("گودرز", false)
                    ),
                    explanation = "آرش کمانگیر جان پاک خود را در چله کمان گذاشت و تیر مرز ایران را از دماوند رها نمود."
                ),
                QuizQuestion(
                    id = 309,
                    question = "چکامه مشهور «ای دیو سپید پای در بند» از سروده‌های کدام شاعر برجسته است؟",
                    options = listOf(
                        QuizOption("ملک‌الشعرای بهار", true),
                        QuizOption("نیما یوشیج", false),
                        QuizOption("شهریار", false),
                        QuizOption("پروین اعتصامی", false)
                    ),
                    explanation = "محمدتقی بهار این شاهکار ملی را در ستایش استواری دماوند سرود."
                )
            ),
            rewardCoins = 70
        ),

        4 to GameStage(
            stageNumber = 4,
            realmId = "shahnameh",
            stageTitle = "پیر فرزانه طوس",
            targetWords = listOf("فردوسی", "فرد", "سود", "دیو", "سرد", "روس"),
            bonusWords = listOf("سیر", "دیر", "رود", "سیف", "دور"),
            letters = listOf('ف', 'ر', 'د', 'و', 'س', 'ی'),
            knowledgeCard = KnowledgeCard(
                id = 104,
                title = "حکیم ابوالقاسم فردوسی",
                realmTitle = "باغ سخن",
                summary = "دهقان فرزانه طوس که با سرودن شاهنامه، کاخی بلند از نظم برافراشت تا زبان پارسی تا ابد زنده و سرافراز بماند.",
                wisdomQuote = "بسی رنج بردم در این سال سی / عجم زنده کردم بدین پارسی",
                era = "قرن چهارم هجری",
                iconSymbol = "🖋️"
            ),
            visualRiddle = VisualRiddle(
                id = 204,
                title = "زنده‌کننده نام و زبان ایران",
                clueText = "حکیم دانای خراسان و آفریننده بزرگ‌ترین حماسه منظوم جهان در طوس.",
                answerWord = "فردوسی",
                visualType = "FERDOWSI",
                scrambledLetters = listOf('ف', 'ر', 'د', 'و', 'س', 'ی', 'ط', 'و')
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 310,
                    question = "آرامگاه حکیم ابوالقاسم فردوسی در کدام کهن‌شهر ایران قرار دارد؟",
                    options = listOf(
                        QuizOption("طوس (مشهد)", true),
                        QuizOption("نیشابور", false),
                        QuizOption("شیراز", false),
                        QuizOption("بلخ", false)
                    ),
                    explanation = "آرامگاه فردوسی در طوس، زادگاه و دیار این حکیم خردمند واقع شده است."
                ),
                QuizQuestion(
                    id = 311,
                    question = "شاهنامه فردوسی به تقریب از چند بیت شعر فاخر حماسی تشکیل شده است؟",
                    options = listOf(
                        QuizOption("حدود ۵۰ تا ۶۰ هزار بیت", true),
                        QuizOption("۱۰ هزار بیت", false),
                        QuizOption("۲۵ هزار بیت", false),
                        QuizOption("۱۰۰ هزار بیت", false)
                    ),
                    explanation = "شاهنامه شامل نزدیک به ۶۰ هزار بیت در بحر متقارب مثمن محذوف است."
                ),
                QuizQuestion(
                    id = 312,
                    question = "فردوسی چند سال از عمر گرانمایه خود را وقف سرودن و نگارش شاهنامه کرد؟",
                    options = listOf(
                        QuizOption("سی سال", true),
                        QuizOption("ده سال", false),
                        QuizOption("بیست سال", false),
                        QuizOption("چهل سال", false)
                    ),
                    explanation = "فردوسی خود می‌فرماید: پی افکندم از نظم کاخی بلند / که از باد و باران نیابد گزند / بسی رنج بردم در این سال سی..."
                )
            ),
            rewardCoins = 80
        ),

        5 to GameStage(
            stageNumber = 5,
            realmId = "shahnameh",
            stageTitle = "شاهزاده پاک‌نهاد آتش",
            targetWords = listOf("سیاوش", "سویش", "یاس", "سوی", "شای"),
            bonusWords = listOf("سوش", "واش", "آس"),
            letters = listOf('س', 'ی', 'ا', 'و', 'ش'),
            knowledgeCard = KnowledgeCard(
                id = 105,
                title = "شاهزاده سیاوش",
                realmTitle = "باغ سخن",
                summary = "سیاوش نماد مظلومیت، پاکی و جوانمردی است؛ پهلوانی راست‌کردار که برای اثبات عصمت خود سوار بر اسب از دل آتش خروشان گذشت.",
                wisdomQuote = "چو خواهی که پیدا کنی راستی / برون آور از دل کژی و کاستی",
                era = "دوره پهلوانی شاهنامه",
                iconSymbol = "🔥"
            ),
            visualRiddle = VisualRiddle(
                id = 205,
                title = "سربلند از آزمون آتش",
                clueText = "شاهزاده‌ای پاک‌دامن که گیاه سرخ پرسیوشان از خون به ناحق ریخته او بر خاک رویید.",
                answerWord = "سیاوش",
                visualType = "SIAVASH",
                scrambledLetters = listOf('س', 'ی', 'ا', 'و', 'ش', 'پ', 'ا', 'ک')
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 313,
                    question = "سیاوش برای اثبات پاک‌دامنی و راستی خویش از میان چه گذشت؟",
                    options = listOf(
                        QuizOption("آتش شعله‌ور (ور گرم)", true),
                        QuizOption("رود خروشان جیحون", false),
                        QuizOption("بیشه پر از پلنگ", false),
                        QuizOption("غار تاریک دیوان", false)
                    ),
                    explanation = "سیاوش با اسب سیاه خود شبرنگ از میان کوه آتش به سلامت و سپیدی بیرون آمد."
                ),
                QuizQuestion(
                    id = 314,
                    question = "گیاه خودرویی که در اساطیر ایرانی از جای ریخته شدن خون سیاوش بر زمین رویید چه نام دارد؟",
                    options = listOf(
                        QuizOption("پرسیوشان (خون سیاوش)", true),
                        QuizOption("گل لاله", false),
                        QuizOption("نیلوفر آبی", false),
                        QuizOption("گل ارغوان", false)
                    ),
                    explanation = "به باور ایرانیان، گیاه دارویی پرسیوشان نشانه جاودانگی پاکی و خون بی‌گناه است."
                ),
                QuizQuestion(
                    id = 315,
                    question = "فرزند دادگر سیاوش که پس از او پادشاه دادگستر ایران شد چه نام داشت؟",
                    options = listOf(
                        QuizOption("کیخسرو", true),
                        QuizOption("لهراسب", false),
                        QuizOption("گشتاسب", false),
                        QuizOption("بهمن", false)
                    ),
                    explanation = "کیخسرو فرزند سیاوش و فرنگیس، کین سیاوش را ستاند و نماد پادشاه آرمانی شد."
                )
            ),
            rewardCoins = 100
        )
    )
}
