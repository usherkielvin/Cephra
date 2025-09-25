// Simple shared i18n for User web
(function() {
    if (window.CephraI18n) return;

    const dictionary = {
        en: {
            // Common nav
            navDashboard: 'Dashboard',
            navLink: 'Link',
            navHistory: 'History',
            navProfile: 'Profile',
            navBack: 'Back',
            // Auth/login/register
            brandSubtitle: 'Your Electric Vehicle Charging Platform',
            brandLoginDesc: 'Sign in to access your charging dashboard and manage your electric vehicle charging sessions.',
            brandRegisterDesc: 'Create your account to access your charging dashboard and manage your electric vehicle charging sessions.',
            loginTitle: 'Sign In',
            loginSubtitle: 'Enter your credentials to access your account',
            username: 'Username',
            password: 'Password',
            forgotPassword: 'Forgot password?',
            createAccountLink: 'Create account',
            submitLogin: 'Sign In',
            registerTitle: 'Create Account',
            registerSubtitle: 'Enter your information to get started',
            firstname: 'Firstname',
            lastname: 'Lastname',
            email: 'Email',
            confirmPassword: 'Confirm password',
            alreadyHaveAccount: 'Already have an account? Sign in',
            submitRegister: 'Create Account',

            // History page
            historyTitle: 'Transaction History',
            historySubtitle: 'View and manage your payment transaction records',
            searchPlaceholder: 'Search transactions...',
            filterAllStatus: 'All Status',
            filterCompleted: 'Completed',
            filterPending: 'Pending',
            filterFailed: 'Failed',
            filterAllMethods: 'All Methods',
            filterCash: 'Cash',
            filterGCash: 'GCash',
            clearFilters: 'Clear Filters',
            thTransactionId: 'Transaction ID',
            thStatus: 'Status',
            thAmount: 'Amount',
            thMethod: 'Method',
            thReference: 'Reference',
            thDate: 'Date',
            thActions: 'Actions',
            noTransactions: 'No Transactions Yet',
            noTransactionsHint: "You haven't made any payments. Start by linking your account.",
            view: 'View',
            modalTitle: 'Transaction Details',
            modalClose: 'Close',
            modalId: 'Transaction ID:',
            modalStatus: 'Status:',
            modalAmount: 'Amount:',
            modalMethod: 'Payment Method:',
            modalReference: 'Reference Number:',
            modalDate: 'Date & Time:'
        },
        fil: {
            navDashboard: 'Dashboard',
            navLink: 'Link',
            navHistory: 'Kasaysayan',
            navProfile: 'Profile',
            navBack: 'Bumalik',
            brandSubtitle: 'Iyong Electric Vehicle Charging Platform',
            brandLoginDesc: 'Mag-login para ma-access ang iyong dashboard at pamahalaan ang iyong charging sessions.',
            brandRegisterDesc: 'Gumawa ng account para ma-access ang dashboard at pamahalaan ang iyong charging sessions.',
            loginTitle: 'Mag-login',
            loginSubtitle: 'Ilagay ang iyong kredensyal',
            username: 'Username',
            password: 'Password',
            forgotPassword: 'Nakalimutan ang password?',
            createAccountLink: 'Gumawa ng account',
            submitLogin: 'Mag-login',
            registerTitle: 'Gumawa ng Account',
            registerSubtitle: 'Ilagay ang iyong impormasyon para makapagsimula',
            firstname: 'Pangalan',
            lastname: 'Apelyido',
            email: 'Email',
            confirmPassword: 'Kumpirmahin ang password',
            alreadyHaveAccount: 'May account na? Mag-login',
            submitRegister: 'Gumawa ng Account',

            historyTitle: 'Kasaysayan ng Transaksiyon',
            historySubtitle: 'Tingnan at pamahalaan ang iyong mga bayad',
            searchPlaceholder: 'Maghanap ng transaksiyon...',
            filterAllStatus: 'Lahat ng Status',
            filterCompleted: 'Kumpleto',
            filterPending: 'Nakabinbin',
            filterFailed: 'Bigo',
            filterAllMethods: 'Lahat ng Paraan',
            filterCash: 'Cash',
            filterGCash: 'GCash',
            clearFilters: 'I-clear ang Mga Filter',
            thTransactionId: 'Transaction ID',
            thStatus: 'Status',
            thAmount: 'Halaga',
            thMethod: 'Paraan',
            thReference: 'Referencia',
            thDate: 'Petsa',
            thActions: 'Mga Aksyon',
            noTransactions: 'Wala pang Transaksiyon',
            noTransactionsHint: 'Wala ka pang bayad. Magsimula sa pag-link ng iyong account.',
            view: 'Tingnan',
            modalTitle: 'Detalye ng Transaksiyon',
            modalClose: 'Isara',
            modalId: 'Transaction ID:',
            modalStatus: 'Status:',
            modalAmount: 'Halaga:',
            modalMethod: 'Paraan ng Bayad:',
            modalReference: 'Reference Number:',
            modalDate: 'Petsa at Oras:'
        },
        ceb: {
            navDashboard: 'Dashboard',
            navLink: 'Link',
            navHistory: 'Kasaysayan',
            navProfile: 'Profile',
            navBack: 'Balik',
            brandSubtitle: 'Imong Electric Vehicle Charging Platform',
            brandLoginDesc: 'Sulod aron ma-access ang imong dashboard ug pagdumala sa charging sessions.',
            brandRegisterDesc: 'Paghimo og account aron ma-access ang dashboard ug pagdumala sa charging sessions.',
            loginTitle: 'Sulod',
            loginSubtitle: 'Ibutang ang imong kredensyal',
            username: 'Username',
            password: 'Password',
            forgotPassword: 'Nakalimot sa password?',
            createAccountLink: 'Paghimo og account',
            submitLogin: 'Sulod',
            registerTitle: 'Paghimo og Account',
            registerSubtitle: 'Ibutang ang imong impormasyon aron magsugod',
            firstname: 'Ngalan',
            lastname: 'Apelyido',
            email: 'Email',
            confirmPassword: 'Kompirmar sa password',
            alreadyHaveAccount: 'Naay account na? Sulod',
            submitRegister: 'Paghimo og Account',

            historyTitle: 'Kasaysayan sa Transaksiyon',
            historySubtitle: 'Tan-awa ug dumala ang imong mga bayad',
            searchPlaceholder: 'Pangita og transaksiyon...',
            filterAllStatus: 'Tanang Status',
            filterCompleted: 'Humana',
            filterPending: 'Pending',
            filterFailed: 'Napakyas',
            filterAllMethods: 'Tanang Pamaagi',
            filterCash: 'Cash',
            filterGCash: 'GCash',
            clearFilters: 'Hapsayon ang mga Filter',
            thTransactionId: 'Transaction ID',
            thStatus: 'Status',
            thAmount: 'Kantidad',
            thMethod: 'Pamaagi',
            thReference: 'Referencia',
            thDate: 'Petsa',
            thActions: 'Mga Aksyon',
            noTransactions: 'Walay Transaksiyon',
            noTransactionsHint: 'Wala pa kay bayad. Sugdi sa pag-link sa imong account.',
            view: 'Tan-awa',
            modalTitle: 'Detalye sa Transaksiyon',
            modalClose: 'Sirad-an',
            modalId: 'Transaction ID:',
            modalStatus: 'Status:',
            modalAmount: 'Kantidad:',
            modalMethod: 'Pamaagi sa Bayad:',
            modalReference: 'Reference Number:',
            modalDate: 'Petsa ug Oras:'
        },
        zh: {
            navDashboard: '仪表盘',
            navLink: '链接',
            navHistory: '历史',
            navProfile: '个人资料',
            navBack: '返回',
            brandSubtitle: '您的电动车充电平台',
            brandLoginDesc: '登录以访问仪表盘并管理充电会话。',
            brandRegisterDesc: '创建账户以访问仪表盘并管理充电会话。',
            loginTitle: '登录',
            loginSubtitle: '输入凭据以访问账户',
            username: '用户名',
            password: '密码',
            forgotPassword: '忘记密码？',
            createAccountLink: '创建账户',
            submitLogin: '登录',
            registerTitle: '创建账户',
            registerSubtitle: '输入信息以开始使用',
            firstname: '名',
            lastname: '姓',
            email: '邮箱',
            confirmPassword: '确认密码',
            alreadyHaveAccount: '已有账户？登录',
            submitRegister: '创建账户',

            historyTitle: '交易历史',
            historySubtitle: '查看并管理您的支付记录',
            searchPlaceholder: '搜索交易...',
            filterAllStatus: '全部状态',
            filterCompleted: '已完成',
            filterPending: '待处理',
            filterFailed: '失败',
            filterAllMethods: '全部方式',
            filterCash: '现金',
            filterGCash: 'GCash',
            clearFilters: '清除筛选',
            thTransactionId: '交易号',
            thStatus: '状态',
            thAmount: '金额',
            thMethod: '方式',
            thReference: '参考号',
            thDate: '日期',
            thActions: '操作',
            noTransactions: '暂无交易',
            noTransactionsHint: '您还没有支付记录。请先链接账户。',
            view: '查看',
            modalTitle: '交易详情',
            modalClose: '关闭',
            modalId: '交易号:',
            modalStatus: '状态:',
            modalAmount: '金额:',
            modalMethod: '支付方式:',
            modalReference: '参考号:',
            modalDate: '日期与时间:'
        }
    };

    const langLabel = { en: 'EN', fil: 'Fil', ceb: 'Bisaya', zh: '中文' };

    function getLang() { return localStorage.getItem('selectedLanguage') || 'en'; }

    function setLang(code) { localStorage.setItem('selectedLanguage', code); }

    function applyI18n(root) {
        const lang = getLang();
        const dict = dictionary[lang] || dictionary.en;
        const elements = (root || document).querySelectorAll('[data-i18n]');
        elements.forEach(el => {
            const key = el.getAttribute('data-i18n');
            if (!key) return;
            const value = dict[key];
            if (typeof value === 'string') {
                // Removed placeholder setting for inputs
                if (!(el.placeholder !== undefined && el.tagName === 'INPUT')) {
                    el.textContent = value;
                }
            }
        });

        // Update any language indicator text
        document.querySelectorAll('.language-text').forEach(node => {
            node.textContent = langLabel[lang] || 'EN';
        });
    }

    function wireLanguageDropdown() {
        const trigger = document.getElementById('languageBtn') || document.getElementById('mobileLanguageBtn');
        const dropdown = document.getElementById('languageDropdown') || document.getElementById('mobileLanguageDropdown');
        if (!trigger || !dropdown) return;

        trigger.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdown.classList.toggle('show');
            trigger.classList.toggle('active');
        });

        dropdown.querySelectorAll('.language-option').forEach(opt => {
            opt.addEventListener('click', function() {
                const code = this.getAttribute('data-lang');
                setLang(code);
                applyI18n();
                dropdown.classList.remove('show');
                trigger.classList.remove('active');
            });
        });

        document.addEventListener('click', function(e) {
            if (!trigger.contains(e.target) && !dropdown.contains(e.target)) {
                dropdown.classList.remove('show');
                trigger.classList.remove('active');
            }
        });
    }

    window.CephraI18n = {
        apply: applyI18n,
        getLang,
        setLang,
        dict: dictionary
    };

    document.addEventListener('DOMContentLoaded', function() {
        applyI18n();
        wireLanguageDropdown();
    });
})();