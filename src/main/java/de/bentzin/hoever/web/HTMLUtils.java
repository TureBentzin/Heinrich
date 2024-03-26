package de.bentzin.hoever.web;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class HTMLUtils {
    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(HTMLUtils.class);

    public static List<DataBlock> extractDataBlocks(@NotNull String html) {
        List<DataBlock> dataBlocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"largeText\">(.*?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String blockContent = matcher.group(1);
            List<String> urls = extractUrls(blockContent);
            List<String> names = extractNames(blockContent);
            String topic = extractTopic(blockContent);
            DataBlock dataBlock = new DataBlock(urls, names, topic);
            dataBlocks.add(dataBlock);
        }
        return dataBlocks;
    }

    private static List<String> extractUrls(@NotNull String blockContent) {
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a\\s+href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return urls;
    }

    private static List<String> extractNames(@NotNull String blockContent) {
        List<String> names = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a\\s+href=\"[^\"]*\">([^<]*)</a>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    private static String extractTopic(@NotNull String blockContent) {
        Pattern pattern = Pattern.compile("<li>(.*?)</li>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(blockContent);
        while (matcher.find()) {
            String topicCandidate = matcher.group(1);
            //further validation probably needed
            return topicCandidate;
        }
        return null;
    }

    public static class DataBlock {
        @NotNull
        private List<String> urls;
        @NotNull
        private List<String> names;
        @NotNull
        private String topic;

        public DataBlock(@NotNull List<String> urls, @NotNull List<String> names, @NotNull String topic) {
            this.urls = urls;
            this.names = names;
            this.topic = topic;
        }

        @NotNull
        public List<String> getUrls() {
            return urls;
        }

        @NotNull
        public List<String> getNames() {
            return names;
        }

        @NotNull
        public String getTopic() {
            return topic;
        }
    }


    public static void main(String[] args) {

        List<HTMLUtils.DataBlock> dataBlocks = HTMLUtils.extractDataBlocks(DEMO);
        for (HTMLUtils.DataBlock dataBlock : dataBlocks) {
            logger.info("Topic: {}", dataBlock.getTopic());
            logger.info(dataBlock.getNames().toString());
            logger.info(dataBlock.getUrls().toString());
        }
    }

    public static String DEMO = """
            <body data-new-gr-c-s-check-loaded="8.911.0" data-gr-ext-installed="" style=""><aside><a href="javascript:EyeAbleAPI.toggleToolbar()" class="eyeAble_hiddenOpener" ealangid="accessibleLinkText" style="height: 1px !important; width: 1px !important; margin: -1px !important; overflow: hidden !important; clip: rect(1px, 1px, 1px, 1px) !important; position: fixed !important; top: -10000px !important; left: -10000px !important; display: inline;">Visuelle Assistenzsoftware öffnen. Mit der Tastatur erreichbar über ALT + 1</a></aside>
                        
                        
            <div id="hilfsnavi" class="invisible">
                <a href="#hilfsnavi_portals">zu den FH-Portalen</a>,
                <a href="#hilfsnavi_login">zum Login</a>,
                <a href="#hilfsnavi_language">zur Sprachumschaltung</a>,
                <a href="#hilfsnavi_mainsearch">zur Seitensuche</a>,
                <a href="#hilfsnavi_mainnav">zur Haupt-Navigation</a>,
                <a href="#hilfsnavi_breadcrumbRow">zur Seiten-Navigation (inclusive Breadcrumb)</a>,
                <a href="#hilfsnavi_main">zum Seiteninhalt</a>,
            </div>
                        
                        
                        
            <!-- Webcode:  -->
            <header class="sticky-top bg-white" nav="headline">
                <div class="container" id="fh_topNavRow">
                    <div class="row row-cols-auto align-items-start justify-content-between">
                        <nav class="fh_topnav navbar col col-md-12 col-lg order-md-1 order-lg-3 justify-content-start justify-content-md-end justify-content-lg-end">
                            <div class="row small" id="navbarTopContent">
                               \s
            <div id="fh_portals" class="col nav-item dropdown">
                <span id="hilfsnavi_portals" class="invisible"><a href="#hilfsnavi_login" tabindex="7">Portale überspringen</a></span>
                <span aria-controls="portaleDropdown" class="" data-bs-target="#portaleDropdown" data-bs-toggle="offcanvas" id="navbarPortale" tabindex="7" onkeydown="evalKey()">
                    Portale
                </span>
                <div aria-labelledby="navbarPortale" class="offcanvas bg-white text-dark offcanvas-end" id="portaleDropdown" tabindex="-1">
                    <div class="offcanvas-header">
                        <header>
                            Portale
                        </header>
                        <button aria-label="Close" class="btn-close text-end " data-bs-dismiss="offcanvas" type="button"></button>
                    </div>
                    <dl>
                       \s
                           \s
                                    <a href="https://mail.fh-aachen.de" target="_blank">
                                        <dt>Webmailer</dt>
                                        <dd>Mail-Portal der FH Aachen</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://h1.fh-aachen.de/" target="_blank">
                                        <dt>HisInOne</dt>
                                        <dd>Bewerbungsportal und Online-Studienservice</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://qis.fh-aachen.de" target="_blank">
                                        <dt>QIS</dt>
                                        <dd>Prüfungsverwaltung der FH Aachen</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://ili.fh-aachen.de" target="_blank">
                                        <dt>ILIAS</dt>
                                        <dd>eLearning-Plattform der FH Aachen</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://www.campusoffice.fh-aachen.de/" target="_blank">
                                        <dt>campusOffice</dt>
                                        <dd>Studienplaner</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://www.campus.fh-aachen.de/" target="_blank">
                                        <dt>Campus</dt>
                                        <dd>Raum- und Vorlesungsverzeichnis</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://services.fh-aachen.de" target="_blank">
                                        <dt>Services-Seite</dt>
                                        <dd>Self-Services rund um Accounts, FH Karte, Zeiterfassung und vieles mehr</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://fhb-aachen.digibib.net/search/katalog" target="_blank">
                                        <dt>Bibliothekskatalog</dt>
                                        <dd>Online-Katalog zur Literatursuche</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://hilfecenter.fh-aachen.de" target="_blank">
                                        <dt>Hilfecenter</dt>
                                        <dd>Support-Plattform der Hochschule</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://www.campus.fh-aachen.de/campus/all/search.asp?mode=addressrwth" target="_blank">
                                        <dt>Telefonbuch</dt>
                                        <dd>Kontaktübersicht zu allen Beschäftigten der FH</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://www.fh-aachen.de/downloads" target="_blank">
                                        <dt>Downloads</dt>
                                        <dd>Downloadcenter der FH Aachen</dd>
                                    </a>
                               \s
                       \s
                           \s
                                    <a href="https://www.fhshop-aachen.de/" target="_blank">
                                        <dt>FH-Shop</dt>
                                        <dd>offizieller Online-Shop der FH Aachen</dd>
                                    </a>
                               \s
                       \s
                    </dl>
                </div>
            </div>
                        
                        
                               \s
                        
            <div id="fh_login" class="col nav-item dropdown">
                <span id="hilfsnavi_login" class="invisible"><a href="#hilfsnavi_language" tabindex="8">Login überspringen</a></span>
               \s
                        <span aria-controls="#loginDropdown" class="" id="navbarLoginOALogin" tabindex="8" onkeydown="evalKey()" onclick="document.getElementById('oauth2_provider_keycloak_button').click()">
                                Login
                            </span>
                   \s
                <div aria-labelledby="navbarLogin" class="offcanvas bg-white text-dark offcanvas-end" id="loginDropdown" tabindex="-1">
                   \s
                            <div class="offcanvas-header">
                                <header>
                                    Anmelden
                                </header>
                                <button aria-label="Close" class="btn-close text-end" data-bs-dismiss="offcanvas" type="button"></button>
                            </div>
                            <form action="" method="post" onsubmit="" target="_top">
                                <ul>
                                   \s
                                    <li>
                                        <div>
                                            <button type="submit" id="oauth2_provider_keycloak_button" name="oauth2-provider" value="keycloak">
                                                <span class="t3js-icon icon icon-size-large icon-state-default icon-oauth2-keycloak" data-identifier="oauth2-keycloak">
            	<span class="icon-markup">
            <img src="/typo3conf/ext/fhac_design_2021/Resources/Public/Icons/2FA-fhac.png" width="48" height="48" alt="">
            	</span>
            	
            </span>
                                                zentrale Anmeldung der FH-Aachen
                                            </button>
                                            <br><label>
                                                Funktioniert nur nach vorheriger Freischaltung bei normalem Login
                                            </label>
                                        </div>
                                    </li>
                                    <hr><br><br>
                                   \s
                                    <li>
                                        <div>
                                            <label for="user">FH-Kennung</label><br>
                                            <input id="user" name="user" type="text" value="">
                                        </div>
                                    </li>
                                    <li>
                                        <div>
                                            <label for="pass">Passwort</label><br>
                                            <input data-rsa-encryption="" id="pass" name="pass" type="password" value="">
                                        </div>
                                    </li>
                                    <li>
                                        <div>
                                            <input name="submit" type="submit" value="Anmelden">
                                            <span class="felogin-hidden">
                                                <input name="logintype" type="hidden" value="login">
                                                <input name="pid" type="hidden" value="9">
                                                <input name="redirect_url" type="hidden" value="">
                                                <input name="tx_felogin_pi1[noredirect]" type="hidden" value="1">
                                            </span>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                       \s
                </div>
            </div>
                        
                        
                               \s
            <div id="fh_accessibility" class="col nav-item dropdown">
                <span id="hilfsnavi_accessibility" class="invisible"><a href="#hilfsnavi_language" tabindex="2">Barrierefreiheit überspringen</a></span>
                <span aria-controls="#accessibilityDropdown" class="" data-bs-target="#accessibilityDropdown" data-bs-toggle="offcanvas" id="navbarAccessibility" tabindex="2" onkeydown="evalKey()">
                    Barrierefreiheit
                </span>
                <div aria-labelledby="navbarAccessibility" class="offcanvas bg-white text-dark offcanvas-end" id="accessibilityDropdown" tabindex="-1">
                    <div class="offcanvas-header">
                        <header>
                            Barrierefreiheit
                        </header>
                        <button id="accessibilityDropdownClose" tabindex="2" aria-label="Close" class="btn-close text-end" data-bs-dismiss="offcanvas" type="button"></button>
                    </div>
                    <dl>
                        <a role="button" class="eyeAble_customToolOpenerClass1" tabindex="2" onclick="document.getElementById('accessibilityDropdownClose').click();setTimeout(EyeAbleAPI.toggleToolbar,300);" onkeydown="evalKey()" title="Visuelle Assistenzsoftware öffnen. Mit der Tastatur erreichbar über ALT + 1" id="eyeAble_customToolOpenerID1">
                            <dt>Visuelle Hilfen</dt>
                            <dd>Aktivieren Sie hier die Assistenzsoftware zur Verbesserung der Zugänglichkeit unserer Seiten.</dd>
                        </a>
                       \s
                           \s
                                    <a href="https://www.fh-aachen.de/barrierefreiheit" target="_self" tabindex="7">
                                        <dt>Erklärung zur Barrierefreiheit</dt>
                                        <dd>Alle öffentlichen Stellen in der EU sind verpflichtet ihre Internetseiten, Apps sowie ihre Interseiten barrierefrei zu gestalten und eine Erklärung zur Barrierefreiheit zur veröffentlichen.</dd>
                                    </a>
                               \s
                       \s
                    </dl>
                </div>
            </div>
                        
                        
                               \s
            <div id="fh_language" class="col nav-item" style="display: inherit;">
                <span id="hilfsnavi_language" class="invisible"><a href="#hilfsnavi_mainsearch" tabindex="1">Sprachumschaltung überspringen</a></span>
               \s
                    <span class="active">De</span>
                    <span class="language-trenner">|</span>
                    <span class=""><a tabindex="1" href="/en/people/hoever/lehrveranstaltungen/hoehere-mathematik-1/wochenplaene-2023/24-hoehere-mathematik-1">En</a></span>
               \s
            </div>
                        
                        
                        
                            </div>
                        </nav>
                        <nav id="fh_mainsearch" class="fh_mainsearch col-auto col-md-auto col-lg-auto order-md-2 order-lg-2 container navbar">
                            <span id="hilfsnavi_mainsearch" class="invisible"><a href="#hilfsnavi_mainnav">Seitensuche überspringen</a></span>
                           \s
            <div class="row">
                <div class="col-12 col-md mm_search">
                    <form id="googlesuche" onsubmit="doSearch();return false;">
                        <span class="input-group">
                            <span class="input-group-btn" id="googlesuche_button" aria-controls="#sucheDropdown" aria-label="Toggle Suche" data-bs-target="#sucheDropdown" data-bs-toggle="offcanvas">
                                <button aria-label="Suchen" class="btn btn-default" type="submit" tabindex="-1">
                                    <i class="bi bi-search"></i>
                                </button>
                            </span>
                            <input aria-label="Suchfeld" class="form-control tx-indexedsearch-searchbox-sword" id="googlesuche-sword" name="search" placeholder="Suche" type="text" value="" tabindex="6">
                        </span>
                    </form>
                </div>
                <div class="offcanvas bg-white text-dark offcanvas-end" id="sucheDropdown">
                    <div class="offcanvas-header">
                        <headline>
                            Suche
                        </headline>
                        <button aria-label="Close" class="btn-close text-end" data-bs-dismiss="offcanvas" type="button"></button>
                    </div>
                    <script async="" src="https://cse.google.com/cse.js?cx=010420090661851522299:dfpzfkz9vi0">
                    </script>
                    <div id="___gcse_0"><div class="gsc-control-cse gsc-control-cse-de"><div class="gsc-control-wrapper-cse" dir="ltr"><form class="gsc-search-box gsc-search-box-tools" accept-charset="utf-8"><table cellspacing="0" cellpadding="0" role="presentation" class="gsc-search-box"><tbody><tr><td class="gsc-input eA_fcB"><div class="gsc-input-box" id="gsc-iw-id1"><table cellspacing="0" cellpadding="0" style="width: 100%; padding: 0px;" role="presentation" id="gs_id50" class="gstl_50 gsc-input eA_fcB"><tbody><tr><td id="gs_tti50" class="gsib_a"><input autocomplete="off" type="text" size="10" class="gsc-input eA_fcB" name="search" title="suchen" aria-label="suchen" style="width: 100%; padding: 0px; border: medium; margin: 0px; height: auto; background: rgb(255, 255, 255) url(&quot;https://www.google.com/cse/static/images/1x/de/branding.png&quot;) left center no-repeat; outline: none;" id="gsc-i-id1" dir="ltr" spellcheck="false"></td><td class="gsib_b"><div class="gsst_b" id="gs_st50" dir="ltr"><a class="gsst_a" href="javascript:void(0)" style="display: none;" title="Suchfeldeingaben löschen" role="button"><span class="gscb_a" id="gs_cb50" aria-hidden="true">×</span></a></div></td></tr></tbody></table></div></td><td class="gsc-search-button eyeAbleContrastSkip"><button class="gsc-search-button gsc-search-button-v2 eyeAbleContrastSkip"><svg width="13" height="13" viewBox="0 0 13 13"><title>suchen</title><path d="m4.8495 7.8226c0.82666 0 1.5262-0.29146 2.0985-0.87438 0.57232-0.58292 0.86378-1.2877 0.87438-2.1144 0.010599-0.82666-0.28086-1.5262-0.87438-2.0985-0.59352-0.57232-1.293-0.86378-2.0985-0.87438-0.8055-0.010599-1.5103 0.28086-2.1144 0.87438-0.60414 0.59352-0.8956 1.293-0.87438 2.0985 0.021197 0.8055 0.31266 1.5103 0.87438 2.1144 0.56172 0.60414 1.2665 0.8956 2.1144 0.87438zm4.4695 0.2115 3.681 3.6819-1.259 1.284-3.6817-3.7 0.0019784-0.69479-0.090043-0.098846c-0.87973 0.76087-1.92 1.1413-3.1207 1.1413-1.3553 0-2.5025-0.46363-3.4417-1.3909s-1.4088-2.0686-1.4088-3.4239c0-1.3553 0.4696-2.4966 1.4088-3.4239 0.9392-0.92727 2.0864-1.3969 3.4417-1.4088 1.3553-0.011889 2.4906 0.45771 3.406 1.4088 0.9154 0.95107 1.379 2.0924 1.3909 3.4239 0 1.2126-0.38043 2.2588-1.1413 3.1385l0.098834 0.090049z"></path></svg></button></td><td class="gsc-clear-button"><div class="gsc-clear-button" title="Ergebnisse löschen">&nbsp;</div></td></tr></tbody></table></form><div class="gsc-results-wrapper-nooverlay"><div class="gsc-positioningWrapper"><div class="gsc-tabsAreaInvisible"><div aria-label="refinement" role="tab" class="gsc-tabHeader gsc-inline-block gsc-tabhActive">Benutzerdefinierte Suche</div><span class="gs-spacer"> </span></div></div><div class="gsc-positioningWrapper"><div class="gsc-refinementsAreaInvisible"></div></div><div class="gsc-above-wrapper-area-invisible"><div class="gsc-above-wrapper-area-backfill-container"></div><table cellspacing="0" cellpadding="0" role="presentation" class="gsc-above-wrapper-area-container"><tbody><tr><td class="gsc-result-info-container"><div class="gsc-result-info-invisible"></div></td></tr></tbody></table></div><div class="gsc-adBlockInvisible"></div><div class="gsc-wrapper"><div class="gsc-adBlockInvisible"></div><div class="gsc-resultsbox-invisible"><div class="gsc-resultsRoot gsc-tabData gsc-tabdActive"><div><div class="gsc-expansionArea"></div></div></div></div></div></div></div></div></div>
                </div>
                <script>
                    document.getElementById('sucheDropdown').addEventListener('hide.bs.offcanvas', function () {
                        document.getElementById('gs_st50').firstElementChild.click();
                    })
                </script>
            </div>
                        
                        
                        </nav>
                        <nav id="fh_mainnav" class="fh_mainnav col-12 col-md-auto col-lg-auto order-md-1 order-lg-1 container navbar">
                            <span id="hilfsnavi_mainnav" class="invisible"><a href="#hilfsnavi_breadcrumbRow">Haupt-Navigation überspringen</a></span>
                            <div class="row row-cols-auto" id="navbarMainContent">
                               \s
                        
               \s
                        <div class="col fhac_navLink" aria-controls="navbarSideContent" aria-label="Toggle navigation" data-bs-target="#navbarSideContent" data-bs-toggle="offcanvas" onclick="loadNav('https://www.fh-aachen.de/fh-aachen');return true;" onkeydown="evalKey()" tabindex="3">
                            <span class="fh_mainnav_button navbar-toggler" role="button">FH Aachen</span>
                        </div>
                   \s
                        
               \s
                        <div class="col fhac_navLink" aria-controls="navbarSideContent" aria-label="Toggle navigation" data-bs-target="#navbarSideContent" data-bs-toggle="offcanvas" onclick="loadNav('https://www.fh-aachen.de/studium');return true;" onkeydown="evalKey()" tabindex="3">
                            <span class="fh_mainnav_button navbar-toggler" role="button">Studium</span>
                        </div>
                   \s
                        
               \s
                        <div class="col fhac_navLink" aria-controls="navbarSideContent" aria-label="Toggle navigation" data-bs-target="#navbarSideContent" data-bs-toggle="offcanvas" onclick="loadNav('https://www.fh-aachen.de/forschung');return true;" onkeydown="evalKey()" tabindex="3">
                            <span class="fh_mainnav_button navbar-toggler" role="button">Forschung</span>
                        </div>
                   \s
                        
                        
                        
                            </div>
                        </nav>
                    </div>
                </div>
                <div class="container-flex" id="fh_breadcrumbRow">
                    <span id="hilfsnavi_breadcrumbRow" class="invisible"><a href="#hilfsnavi_main">Seiten-Navigation (inclusive Breadcrumb) überspringen</a></span>
                    <div class="container">
                        <div class="row">
                            <nav aria-label="breadcrumb" class="fh_breadcrumb col navbar navbar-light">
                               \s
                        
                <ol class="breadcrumb">
                   \s
                       \s
                               \s
                                   \s
                           \s
                   \s
                       \s
                               \s
                                        <li class="breadcrumb-item  aktiv">
                                           \s
                                                   \s
                                                            <a href="/menschen/hoever" title="Prof. Dr. rer. nat. Dr.-Ing. Georg Hoever" tabindex="9">
                                                                <span>Hoever, Georg, Dr. rer. nat. Dr.-Ing.</span>
                                                            </a>
                                                       \s
                                               \s
                                        </li>
                                   \s
                           \s
                   \s
                       \s
                               \s
                                        <li class="breadcrumb-item  aktiv">
                                           \s
                                                   \s
                                                            <a href="/menschen/hoever/lehrveranstaltungen" title="Lehrveranstaltungen" tabindex="9">
                                                                <span>Lehrveranstaltungen</span>
                                                            </a>
                                                       \s
                                               \s
                                        </li>
                                   \s
                           \s
                   \s
                       \s
                               \s
                                        <li class="breadcrumb-item  aktiv">
                                           \s
                                                   \s
                                                            <a href="/menschen/hoever/lehrveranstaltungen/hoehere-mathematik-1" title="Höhere Mathematik 1" tabindex="9">
                                                                <span>Höhere Mathematik 1</span>
                                                            </a>
                                                       \s
                                               \s
                                        </li>
                                   \s
                           \s
                   \s
                       \s
                               \s
                                        <li class="breadcrumb-item  aktiv current">
                                           \s
                                                    <span title="Sie befinden sich auf dieser Seite">Wochenpläne 2023/24 | Höhere Mathematik 1</span>
                                               \s
                                        </li>
                                   \s
                           \s
                   \s
                    <!--li aria-controls="navbarSideContent" aria-label="Toggle navigation"
                        data-bs-target="#navbarSideContent"
                        data-bs-toggle="offcanvas"
                        class="breadcrumb-item">
                        <button id="sidenavButtonBC1" class="col-1 btn btn-outline-dark">
                            <i class="bi bi-list" id="sideMenuToggleLeft1"></i>
                            <span class="" id="sidenavButtonBCText1">Menü</span>
                        </button>
                    </li-->
                </ol>
                        
                        
                        
                            </nav>
                        </div>
                    </div>
                </div>
                <nav class="offcanvas bg-dark text-light offcanvas-start" id="navbarSideContent" tabindex="-1">
                   \s
                        
            <div class="offcanvas-header text-light">
               \s
                    <ol id="subnavBreadcrumb" class="text-start breadcrumb">
                       \s
                           \s
                                   \s
                                       \s
                               \s
                       \s
                           \s
                                   \s
                                            <li pid="2334" class="breadcrumb-item  aktiv">
                                               \s
                                                       \s
                                                                <a href="/menschen/hoever" title="Prof. Dr. rer. nat. Dr.-Ing. Georg Hoever">
                                                                    <span>Hoever, Georg, Dr. rer. nat. Dr.-Ing.</span>
                                                                </a>
                                                           \s
                                                   \s
                                            </li>
                                       \s
                               \s
                       \s
                           \s
                                   \s
                                            <li pid="20214" class="breadcrumb-item  aktiv">
                                               \s
                                                       \s
                                                                <a href="/menschen/hoever/lehrveranstaltungen" title="Lehrveranstaltungen">
                                                                    <span>Lehrveranstaltungen</span>
                                                                </a>
                                                           \s
                                                   \s
                                            </li>
                                       \s
                               \s
                       \s
                           \s
                                   \s
                                            <li pid="20227" class="breadcrumb-item  aktiv">
                                               \s
                                                       \s
                                                                <a href="/menschen/hoever/lehrveranstaltungen/hoehere-mathematik-1" title="Höhere Mathematik 1">
                                                                    <span>Höhere Mathematik 1</span>
                                                                </a>
                                                           \s
                                                   \s
                                            </li>
                                       \s
                               \s
                       \s
                           \s
                                   \s
                                            <li pid="43626" class="breadcrumb-item  aktiv current">
                                               \s
                                                    <span title="Sie befinden sich auf dieser Seite">Wochenpläne 2023/24 | Höhere Mathematik 1</span>
                                                   \s
                                            </li>
                                       \s
                               \s
                       \s
                    </ol>
               \s
                <button class="btn-close btn-close-white text-end text-light" aria-label="Close" data-bs-dismiss="offcanvas" tabindex="31" type="button" onclick="document.getElementById('subnavContentNAV').innerHTML='';return true;"></button>
            </div>
            <div class="row">
                <div id="subnavContentMAIN" class="row row-cols-auto"><div class="col-auto" tabindex="35" onkeydown="evalKey()" onclick="loadNav('https://www.fh-aachen.de/fh-aachen');">FH Aachen</div><div class="col-auto" tabindex="35" onkeydown="evalKey()" onclick="loadNav('https://www.fh-aachen.de/studium');">Studium</div><div class="col-auto" tabindex="35" onkeydown="evalKey()" onclick="loadNav('https://www.fh-aachen.de/forschung');">Forschung</div></div>
            </div>
            <div id="subnavContentBox" class="offcanvas-body row">
                <div id="subnavContentNAV" class="col"></div>
            </div>
                        
                        
                </nav>
            </header>
                        
               \s
                        
                   \s
               \s
                        
                        
                        
                        
                        
                        
            <main id="fh_main" class="container-fluid">
                <span id="hilfsnavi_main" class="invisible"><a href="#hilfsnavi">zurück zur Hilfsnavigation</a></span>
               \s
                    <a title="Startseite" href="https://www.fh-aachen.de/">
                <span class="" id="fhlogo">
                        <svg id="fhlogo_text" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 112.02 601.71">
                            <g>
                                <path d="m61.22,0c-9.46,0-6.17,14.09-13.14,14.09-2.59,0-4.89-1.79-4.89-6.32,0-2.37.51-4.38,1.39-6.46l-2.15-.44c-.91,1.93-1.46,4.34-1.46,7.01,0,5.88,3.14,8.51,7.27,8.51,9.02,0,5.8-14.06,13.14-14.06,3.54,0,5.66,2.23,5.66,6.57,0,2.88-.73,5.22-2.12,7.34l2.26.44c1.28-2.08,2.12-4.64,2.12-7.78,0-5.84-3.07-8.91-8.07-8.91m5.3,35.48h-11.06v-12.01h-2.04v12.01h-9.75v-13.87l-2.27.11v16.14h27.38v-16.79l-2.26-.11v14.53h0Zm-24.24,7.63c-.69,1.42-1.31,4.09-1.31,6.39,0,7.96,4.23,11.54,13.84,11.54,10.08,0,14.49-3.51,14.49-11.46,0-2.45-.77-4.96-1.64-6.61l-2.08.4c.88,1.64,1.42,3.61,1.42,5.91,0,6.32-2.92,9.27-12.19,9.27-8.69,0-11.54-2.92-11.54-9.16,0-2.26.55-4.45,1.24-5.95l-2.23-.33Zm26.51,23.71h-27.38v2.15l20.19-.04,2.92-.07.03.07-2.48,1.61-20.66,13.69v2.37h27.38v-2.19h-20.66l-2.96.04-.04-.07,2.48-1.61,21.17-14.16v-1.79h0Zm-2.26,39.53h-11.06v-12.01h-2.04v12.01h-9.75v-13.87l-2.27.11v16.14h27.38v-16.79l-2.26-.11v14.53h0Zm2.26,9.5h-27.38v2.41h27.38v-2.41Zm-26.51,7.91c-.69,1.42-1.31,4.09-1.31,6.39,0,7.96,4.23,11.54,13.84,11.54,10.08,0,14.49-3.51,14.49-11.46,0-2.45-.77-4.96-1.64-6.61l-2.08.4c.88,1.64,1.42,3.61,1.42,5.92,0,6.32-2.92,9.27-12.19,9.27-8.69,0-11.54-2.92-11.54-9.16,0-2.26.55-4.45,1.24-5.95l-2.23-.33Zm18.95,22.14c-9.46,0-6.17,14.09-13.14,14.09-2.59,0-4.89-1.79-4.89-6.32,0-2.37.51-4.38,1.39-6.46l-2.15-.44c-.91,1.93-1.46,4.34-1.46,7.01,0,5.88,3.14,8.51,7.27,8.51,9.02,0,5.8-14.06,13.14-14.06,3.54,0,5.66,2.23,5.66,6.57,0,2.88-.73,5.22-2.12,7.34l2.26.44c1.28-2.08,2.12-4.64,2.12-7.78,0-5.84-3.07-8.91-8.07-8.91m-6.24,34.79c9.05,0,11.5,2.99,11.5,9.02v6.5h-22.74v-6.46c0-6.35,2.59-9.05,11.24-9.05m0-2.52c-9.71,0-13.58,3.43-13.58,11.5v8.94h27.38v-8.98c0-7.78-3.8-11.46-13.8-11.46m11.54,40.19h-11.06v-12.01h-2.04v12.01h-9.75v-13.87l-2.27.11v16.14h27.38v-16.79l-2.26-.11v14.53h0Zm2.26,9.5h-27.38v2.41h27.38v-2.41Zm-2.34,20.99h-25.04v2.41h27.38v-15.88l-2.34-.11v13.58Zm-16.9,10.07c4.64,0,6.1,2.19,6.1,6.46v6.32h-12.08v-6.32c0-4.38,1.61-6.46,5.99-6.46m0-2.45c-5.73,0-8.14,2.7-8.14,8.91v8.73h27.38v-2.41h-11.06v-6.32c0-6.28-2.66-8.91-8.18-8.91m0,25.29c4.64,0,6.1,2.19,6.1,6.46v6.32h-12.08v-6.32c0-4.38,1.61-6.46,5.99-6.46m0-2.45c-5.73,0-8.14,2.7-8.14,8.91v8.73h27.38v-2.41h-11.06v-6.32c0-6.28-2.66-8.91-8.18-8.91m-6.35,34.24v-.11l2.81-.91,13.4-4.78v11.61l-13.4-4.86-2.81-.95Zm25.59-9.16v-2.7l-27.38,10.4v2.85l27.38,10.4v-2.59l-7.23-2.59v-13.18l7.23-2.59Zm-25.04,34.28l-2.34.07v16.14h27.38v-2.41h-12.96v-11.61h-2.19v11.61h-9.89v-13.8Zm11.35,24.57c9.2,0,11.86,2.37,11.86,8.4s-2.66,8.47-11.86,8.47-11.83-2.41-11.83-8.43,2.59-8.43,11.83-8.43m0-2.48c-10.15,0-14.13,2.96-14.13,10.92s3.98,10.92,14.13,10.92,14.2-3.03,14.2-10.92-4.02-10.92-14.2-10.92m-13.69,58.19l17.78-9.71h9.6v-2.45h-9.6l-17.78-9.6v2.7l12.82,6.75,2.63,1.28v.11l-2.63,1.31-12.82,6.79v2.81Zm2.34,2.82l-2.34.04v19.5l2.34.07v-8.58h25.04v-2.41h-25.04v-8.62Zm25.04,24.06h-27.38v2.41h27.38v-2.41Zm-7.56,7.41c-9.46,0-6.17,14.09-13.14,14.09-2.59,0-4.89-1.79-4.89-6.32,0-2.37.51-4.38,1.39-6.46l-2.15-.44c-.91,1.93-1.46,4.34-1.46,7.01,0,5.88,3.14,8.51,7.27,8.51,9.02,0,5.8-14.05,13.14-14.05,3.54,0,5.66,2.23,5.66,6.57,0,2.89-.73,5.22-2.12,7.34l2.26.44c1.28-2.08,2.12-4.64,2.12-7.78,0-5.84-3.07-8.91-8.07-8.91m-12.3,24.12c3.98,0,5.59,1.79,5.59,5.77v6.24h-10.95v-6.46c0-3.83,1.53-5.55,5.37-5.55m-.11-2.48c-5,0-7.41,2.52-7.41,8.03v8.87h27.38v-2.41h-12.3v-6.35l12.3-7.01v-2.92l-12.74,7.63c-.44-3.8-2.78-5.84-7.23-5.84m17.71,36.28h-11.06v-12.01h-2.04v12.01h-9.75v-13.87l-2.27.11v16.14h27.38v-16.79l-2.26-.11v14.53h0Zm-25.12,9.35l22.31,7.81,2.77.91v.11l-2.81.91-22.27,7.81v2.67l27.38-9.97v-2.81l-27.38-9.97v2.52h0Zm27.38,24.88h-27.38v2.41h27.38v-2.41Zm0,9.23h-27.38v2.15l20.19-.04,2.92-.07.03.07-2.48,1.61-20.66,13.69v2.37h27.38v-2.19h-20.66l-2.96.04-.04-.07,2.48-1.61,21.17-14.16v-1.79h0Zm-27.38,25.99v2.41h17.49c5.55,0,8.07,2.56,8.07,7.67s-2.45,7.7-8.07,7.7h-17.49v2.41h17.49c7.34,0,10.41-3.72,10.41-10.11s-3.07-10.08-10.41-10.08h-17.49Z"></path>
                                <path d="m27.86,400.43H.48v4.45h15.48l2.92-.07.04.07-2.45,1.64-15.99,10.92v4.38h27.38v-4.49H11.68l-2.96.04-.04-.07,2.48-1.64,16.68-11.68v-3.54Zm-4.49,38.04h-7.59v-10.66h-4.12v10.66h-6.61v-12.56l-4.56.07v17.53h27.38v-18.07l-4.49-.11v13.14h0Zm4.49,10.06H.48v5.04h10.99v11.14H.48v5.04h27.38v-5.04h-11.76v-11.14h11.76v-5.04Zm-26.65,24.72c-.66,1.53-1.21,4.13-1.21,6.35,0,8.54,4.67,12.38,13.95,12.38,9.86,0,14.42-3.87,14.42-12.08,0-2.37-.66-4.97-1.53-6.75l-4.2.58c.69,1.57,1.17,3.4,1.17,5.51,0,5.37-2.3,7.45-9.82,7.45-7.08,0-9.35-2.01-9.35-7.34,0-2.12.44-4.12.99-5.55l-4.42-.55Zm2.96,32.48v-.11l2.85-.77,10.7-3.29v8.32l-10.7-3.32-2.85-.84Zm23.69-7.27v-5.66L.48,502.62v6.02l27.38,9.75v-5.37l-5.88-1.83v-10.95l5.88-1.79Zm-23.69,34.6v-.11l2.85-.77,10.7-3.28v8.32l-10.7-3.32-2.85-.84Zm23.69-7.27v-5.66l-27.38,9.82v6.02l27.38,9.75v-5.37l-5.88-1.83v-10.95l5.88-1.79Zm0,32.93H.48v5.04h10.99v11.14H.48v5.04h27.38v-5.04h-11.76v-11.14h11.76v-5.04Zm-22.71,24.5l-4.67.11v17.49h27.38v-5.04h-11.28v-10.44l-4.42-.04v10.48h-7.01v-12.56Z"></path>
                            </g>
                        </svg>
                        <svg id="fhlogo_farbmarke" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 66.77 601.71">
                            <defs>
                                <style>.cls-1 {fill: #fff;} .cls-2 {fill: #00b1aa;}</style>
                            </defs>
                            <g>
                                <rect class="cls-2" x="0" y=".81" width="66.77" height="200.3"></rect>
                                <rect class="cls-1" x="0" y="201.11" width="66.77" height="200.3"></rect>
                                <rect x="0" y="401.41" width="66.77" height="200.3"></rect>
                            </g>
                        </svg>
                </span>
                    </a>
                        
                        
               \s
                <!--TYPO3SEARCH_begin-->
               \s
            <div class="container">
                <h1>Wochenpläne 2023/24 | Höhere Mathematik 1
                   \s
                </h1>
               \s
                   \s
                        <div aria-controls="navbarSideContent" aria-label="Toggle navigation" data-bs-target="#navbarSideContent" data-bs-toggle="offcanvas" class="fhac_navBurgerHL">
                            <button id="sidenavButtonHL" onclick="loadNav();return true;" class="btn">
                                <i class="bi bi-list" id="sideMenuToggleHL"></i>
                                mehr zum Thema
                            </button>
                        </div>
                   \s
               \s
            </div>
                        
                        
                <div class="fh-content-block"><div class="container"><div class="row">
               \s
                        
                        <div id="c304242" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                    <div class="row">
                        <div class="col-12">
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304213" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            15. Woche (25.01. bis 26.01.): Determinanten
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304213">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304213">
                                <button aria-controls="accordion_content_304213" aria-expanded="true" class="accordion-button" data-bs-target="#accordion_content_304213" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304213" class="accordion-collapse collapse show" id="accordion_content_304213" style="">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304212" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitt 8.5 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-2.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben Teil 2</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-3.mp4" target="_blank" rel="noreferrer">Nachtrag zu einer Übungsaufgabe</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-4.mp4" target="_blank" rel="noreferrer">Determinante: Definition</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-5.mp4" target="_blank" rel="noreferrer">Interpretation und Eigenschaften</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-6.mp4" target="_blank" rel="noreferrer">Determinate und inverse Matrix/LGS</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-2-1.mp4" target="_blank" rel="noreferrer">Laplacescher Entwicklungssatz</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-2-2.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben und Ergänzungen</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant ist der Laplacesche Entwicklungssatz (Satz 8.5.6 und Bemerkung 8.5.7, die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs)</li> 	<br> 	<li>Übungsblatt: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt15.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 15</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu Übungsblatt 15: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt15_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 		<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b8_5__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_5__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe7</a></li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304215" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            14. Woche (18.01. bis 25.01.): Matrizen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304215">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304215">
                                <button aria-controls="accordion_content_304215" aria-expanded="true" class="accordion-button" data-bs-target="#accordion_content_304215" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304215" class="accordion-collapse collapse show" id="accordion_content_304215" style="">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304214" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 8.3 und 8.4 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-1-1.mp4" target="_blank" rel="noreferrer">Übungen zur Gauß-Elimination</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-1-2.mp4" target="_blank" rel="noreferrer">Matrix-Matrix-Addition und -Multiplikation</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-1-3.mp4" target="_blank" rel="noreferrer">Beispiele umd Rechenregeln</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-1-4.mp4" target="_blank" rel="noreferrer">transponierte Matrix</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-2-1.mp4" target="_blank" rel="noreferrer">Quadratische Matrizen; inverse Matrix</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-2-2.mp4" target="_blank" rel="noreferrer">Eigenschaften inverser Matrizen</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-ueb-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-ueb-2.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen Teil 2</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-2.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben Teil 2</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/15-1-3.mp4" target="_blank" rel="noreferrer">Nachtrag zu einer Übungsaufgabe</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant sind die Dinge bzgl. des Rangs (Satz 8.3.15 bis Bemerkung 8.3.17, die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs)<br> 	&nbsp;</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/8_4_quadratische_Form.ggb" title="Opens external link in new window" target="_blank" rel="noreferrer">quadratische Form </a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt14-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 14-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt14-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 14-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 14-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt14-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_3_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 		<li>Übungsblatt 14-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt14-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b8_4__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_4_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304217" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            13. Woche (11.01. bis 18.01.): Matrizen und lineare Gleichungssysteme
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304217">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304217">
                                <button aria-controls="accordion_content_304217" aria-expanded="true" class="accordion-button" data-bs-target="#accordion_content_304217" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304217" class="accordion-collapse collapse show" id="accordion_content_304217" style="">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304216" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 8.1 und 8.2 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: Besprechung der Übungen (wegen technischer Probleme gibt es dazu keine Aufzeichnung), <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-1-1.mp4" target="_blank" rel="noreferrer">Lineare Gleichungssysteme und Matrizen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-1-2.mp4" target="_blank" rel="noreferrer">Struktur der Lösungsmenge eines LGS</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-2-1.mp4" target="_blank" rel="noreferrer">Gaußsches Eliminationsverfahren - Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-2-2.mp4" target="_blank" rel="noreferrer">Gaußsches Eliminationsverfahren - Teil 2</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-ueb-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/13-ueb-2.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen Teil 2</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/14-1-1.mp4" target="_blank" rel="noreferrer">Übungen zur Gauß-Elimination</a>,</li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant ist der Begriff des Rangs (Definition 8.2.8 bis Beispiel 8.2.13, die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs)</li> 	<br> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/8_1_LineareAbbildung.ggb" title="Opens external link in new window" target="_blank" rel="noreferrer">lineare Abbildung </a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/8_2_DrehProjektion1.ggb" title="Opens external link in new window" target="_blank" rel="noreferrer">Projektion-Drehmatrix 1 </a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/8_2_DrehProjektion3.ggb" title="Opens external link in new window" target="_blank" rel="noreferrer">Projektion-Drehmatrix 2 </a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt13-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 13-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt13-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 13-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 13-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt13-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_1_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 		<li>Übungsblatt 13-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt13-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b8_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b8_2_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304219" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            12. Woche (21.12. bis 11.01.): Vektorrechnung: Skalar- und Vektorprodukt, Geraden und Ebenen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304219">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304219">
                                <button aria-controls="accordion_content_304219" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304219" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304219" class="accordion-collapse collapse" id="accordion_content_304219">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304218" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 7.3, 7.4 und 7.5 bis Beispiel 7.5.14 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-2.mp4" target="_blank" rel="noreferrer">Skalarprodukt: Definition; Länge</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-3.mp4" target="_blank" rel="noreferrer">weiteres zur Länge; Abstand</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-4.mp4" target="_blank" rel="noreferrer">Winkel, Orthogonalität</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-5.mp4" target="_blank" rel="noreferrer">Vektorprodukt</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-2-1.mp4" target="_blank" rel="noreferrer">Geraden</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-2-2.mp4" target="_blank" rel="noreferrer">Ebenenen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-2-3.mp4" target="_blank" rel="noreferrer">Schnittpunkte und Abstände</a></li> 			<li>vom Montag gibt's keine Aufzeichnung</li> 		</ul> 		</li> 	</ul> 	Nicht klausurrelevant sind die Abstandsformeln bei Geraden und Ebenen (Satz 7.5.15 und Bemerkung 7.5.16, die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs)</li> 	<br> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_4_Vektorprodukt_1.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Vektorprodukt 1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_4_Vektorprodukt_2.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Vektorprodukt 2</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_5_Geraden.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Gerade</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_5_Abstand_Punkt_Gerade.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Abstand von Punkten zu Geradenpunkten</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_5_Zwei_Geraden.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Schnitt zweier Geraden</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_5_Funktionsapproximation.ggb" title="Opens external link in new window" target="_blank" rel="noreferrer">Approximation von Funktionen </a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt12-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 12-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt12-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 12-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 12-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt12-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b7_3_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_3_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_3__2" title="Opens internal link in current window" target="_blank" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_3__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_4__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_4__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_3_4__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a>, zur Aufgabe 12 gibt es leider kein Video, <a href="https://www.hm-kompakt.de/video?watch=b7_4__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 13</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_4__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 14</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_4__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 15</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_4__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 16</a></li> 		</ul> 		</li> 		<li>Übungsblatt 12-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt12-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b7_5_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_5_3__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b7_5_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, zu den Aufgabe 10 und 11 gibt es leider kein Video</li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304221" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            11. Woche (14.12. bis 21.12.): Integrationstechniken; Vektorrechnung: Einführung
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304221">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304221">
                                <button aria-controls="accordion_content_304221" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304221" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304221" class="accordion-collapse collapse" id="accordion_content_304221">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304220" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 6.3.2 bis 6.3.4 sowie 7.1 und 7.2 (ohne Satz 7.2.8 und Bem. 7.2.9) 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-1-2.mp4" target="_blank" rel="noreferrer">Integration mittels Partialbruchzerlegung</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-1-3.mp4" target="_blank" rel="noreferrer">partielle Integration</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-1-4.mp4" target="_blank" rel="noreferrer">Substitution</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-2-1.mp4" target="_blank" rel="noreferrer">Vektorraum</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-2-2.mp4" target="_blank" rel="noreferrer">Linearkombination</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-ueb-1.mp4" target="_blank" rel="noreferrer">Übungen zur Integration mittels Partialbruchzerlegung und partieller Integration</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-ueb-2.mp4" target="_blank" rel="noreferrer">Ergänzungen und Übungen zu Substitution</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/12-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant ist die alternative Charakterisierung von "linear unabhängig" (Satz 7.2.8 und&nbsp;Bemerkung 7.2.9, die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs)</li> 	<br> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/7_2_Linearkombination.ggb" target="_blank" rel="noreferrer">Linearkombination</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>zweite Probeklausur: 	<ul> 		<li><a href="http://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/pklausur2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Probeklausur </a></li> 		<li><a href="http://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/pklausur2_loes.pdf" target="_blank" rel="noreferrer">Musterlösung schriftlich</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/pklausur2_loes.mp4" target="_blank" rel="noreferrer">Musterlösung mündlich</a> mit <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/pklausur2_loes2.mp4" target="_blank" rel="noreferrer">Korrektur zur letzten Aufgabe</a></li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt11-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 11-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt11-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 11-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 11-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt11-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b6_3_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_3__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_3__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_4__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_4__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a></li> 		</ul> 		</li> 		<li>Übungsblatt 11-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt11-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b7_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b7_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304223" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            10. Woche (07.12. bis 14.12.): Integration: Integral-Definition und Integration mit Stammfunktion
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304223">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304223">
                                <button aria-controls="accordion_content_304223" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304223" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304223" class="accordion-collapse collapse" id="accordion_content_304223">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304222" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 6.1, 6.2 und 6.3.1 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-1.mp4" target="_blank" rel="noreferrer">Einführung des Integrals</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-2.mp4" target="_blank" rel="noreferrer">Integral als Grenzwert von Zwischensummen; Intermezzo: Summenformel</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-3.mp4" target="_blank" rel="noreferrer">uneigentliches Integral</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-4.mp4" target="_blank" rel="noreferrer">Integrationsbereiche</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-5.mp4" target="_blank" rel="noreferrer">Symmetriebetrachtungen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-1-6.mp4" target="_blank" rel="noreferrer">Rechenregeln</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-2-1.mp4" target="_blank" rel="noreferrer">Hauptsatz der Differenzial- und Integralrechnung</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-2-2.mp4" target="_blank" rel="noreferrer">Einfache Stammfunktionen und Integrationstechniken</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-ueb-1.mp4" target="_blank" rel="noreferrer">Übungen zur Riemannschen Zwischesumme</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/10-ueb-2.mp4" target="_blank" rel="noreferrer">numerische Integralberechnung</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/11-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungen zur Integralberechnung mittels Stammfunktion</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/6_1_Riemannsche_Zwischensumme.ggb" target="_blank" rel="noreferrer">Riemannsche Zwischensumme</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/6_1_Ober-_Untersumme.ggb" target="_blank" rel="noreferrer">Ober-/Untersumme</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/6_2_Flaechenfunktion.ggb" target="_blank" rel="noreferrer">Flächenfunktion</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/5_3_4_Taylor-Polynom.ggb" target="_blank" rel="noreferrer">Uneigentliches Integral</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt10-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 10-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt10-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 10-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 10-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt10-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b6_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a></li> 		</ul> 		</li> 		<li>Übungsblatt 10-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt10-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b6_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__12" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__13n" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b6_3_1__14" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304225" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            9. Woche (30.11. bis 07.12.): Regel von de l'Hospital, Newton-Verfahren, Taylor-Polynome
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304225">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304225">
                                <button aria-controls="accordion_content_304225" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304225" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304225" class="accordion-collapse collapse" id="accordion_content_304225">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304224" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 5.3.2, 5.3.3 und 5.3.4 (bis&nbsp;Bemerkung 5.3.23) 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-1-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-1-2.mp4" target="_blank" rel="noreferrer">Regel von de L'Hospital</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-1-3.mp4" target="_blank" rel="noreferrer">Newton-Verfahren</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-2-1.mp4" target="_blank" rel="noreferrer">Taylor-Polynome</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-2-2.mp4" target="_blank" rel="noreferrer">Taylor-Reihen</a></li> 			<li>Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-ueb-1.mp4" target="_blank" rel="noreferrer">Übungen zur Regel von de l'Hospital und zum Newton-Verfahren</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-ueb-2.mp4" target="_blank" rel="noreferrer">Übungen zum Taylor-Polynom</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant ist das Themen "Taylor-Restglied" (Satz 5.3.24 bis Bemerkung 5.3.26.)<br> 	&nbsp;</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/5_3_3_Newtonverfahren.ggb" target="_blank" rel="noreferrer">Newtonverfahren</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/5_3_4_Taylor-Polynom.ggb" target="_blank" rel="noreferrer">Taylor-Polynom</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt9-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 9-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt9-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 9-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 9-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt9-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b5_3_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_3__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_3__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_3__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a></li> 		</ul> 		</li> 		<li>Übungsblatt 9-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt9-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_4__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304227" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            8. Woche (23.11. bis 30.11.): Ableitungsregeln, Kurvendiskussion
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304227">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304227">
                                <button aria-controls="accordion_content_304227" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304227" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304227" class="accordion-collapse collapse" id="accordion_content_304227">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304226" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 5.2 (ohne 5.2.8 bis 5.2.10) und 5.3.1 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-1-2.mp4" target="_blank" rel="noreferrer">Ableitungsregeln</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen/8-1-3.mp4" target="_blank" rel="noreferrer">Quer</a><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-1-3.mp4" target="_blank" rel="noreferrer">b</a>ezüge</li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-2-1.mp4" target="_blank" rel="noreferrer">Zusammenstellung von Ableitungen, Kurvendiskussion Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-2-2.mp4" target="_blank" rel="noreferrer">Kurvendiskussion Teil 2</a></li> 			<li>Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-ueb-2.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 2</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/9-1-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 		&nbsp;</li> 	</ul> 	Nicht klausurrelevant ist das Themen "Ableitung der Umkehrfunktion" (Satz 5.2.8 bis Beispiel 5.2.10; die Nummern beziehen sich auf die <strong>dritte </strong>Auflage des Buchs.)<br> 	&nbsp;</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt8-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 8-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt8-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 8-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 8-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt8-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b5_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>,&nbsp;<a href="https://www.hm-kompakt.de/video?watch=b5_2__12" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__13" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__14" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__15" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__17" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__19" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 12</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__20" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 13</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__21" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 14</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__22" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 15</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__23" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 16</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_2__24" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 17</a></li> 		</ul> 		</li> 		<li>Übungsblatt 8-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt8-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_3_1__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304229" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            7. Woche (16.11. bis 23.11.): Grenzwerte und Stetigkeit; Definition der Ableitung
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304229">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304229">
                                <button aria-controls="accordion_content_304229" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304229" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304229" class="accordion-collapse collapse" id="accordion_content_304229">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304228" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Kapitel 4 (ohne Satz 4.1.6 und Bemerkung 4.1.7) und Abschnitt 5.1 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-1.mp4" target="_blank" rel="noreferrer">Nachträge zu Potenzreihen</a>,&nbsp; <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-2.mp4" target="_blank" rel="noreferrer">Übungen zu Potenzreihen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-3.mp4" target="_blank" rel="noreferrer">Grenzwerte bei Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-4.mp4" target="_blank" rel="noreferrer">Stetigkeit und Bisektionsverfahren</a></li> 			<li>vom Freitag:&nbsp;<a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-2-1.mp4" target="_blank" rel="noreferrer">Differenzialrechnung - Einführung Teil 1,</a> <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-2-2.mp4" target="_blank" rel="noreferrer">Differenzialrechnung - Einführung Teil 2</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-ueb-2.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 2</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/8-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant sind bei den Grenzwerten (Abschnitt 4.1) das Epsilon-Delta-Kriterium (Satz 4.1.6 und Bemerkung 4.1.7; die Nummern beziehen sich jeweils auf die <strong>dritte </strong>Auflage des Buchs.)<br> 	&nbsp;</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/4_2_Bisektionsverfahren.ggb" target="_blank" rel="noreferrer">Bisektionsverfahren</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/5_1_Ableitung_Definition.ggb" target="_blank" rel="noreferrer">Definition der Ableitung</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/5_1_Ableitungsfunktion.ggb" target="_blank" rel="noreferrer">Ableitungsfunktion</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt7-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 7-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt7-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 7-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 7-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt7-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b4_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b4_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 		<li>Übungsblatt 7-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt7-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b5_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__6" title="Opens internal link in current window" target="_blank" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b5_1__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304231" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            6. Woche (09.11. bis 16.11.): Reihen und Potenzreihen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304231">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304231">
                                <button aria-controls="accordion_content_304231" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304231" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304231" class="accordion-collapse collapse" id="accordion_content_304231">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304230" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitt 3.2 bis Beispiel 3.2.19, Abschnitt 3.3 bis Bemerkung 3.3.8 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-1-1.mp4" target="_blank" rel="noreferrer">Reihen - Einführung</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-1-2.mp4" target="_blank" rel="noreferrer">Weiteres zur geometrischen Reihe, Teleskopsumme</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-1-3.mp4" target="_blank" rel="noreferrer">Konvergenz von Reihen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-1-4.mp4" target="_blank" rel="noreferrer">Weiteres zur Konvergenz von Reihen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-1-5.mp4" target="_blank" rel="noreferrer">Potenzreihen</a></li> 			<li>(die Veranstaltung am Freitag fiel aus)</li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben und Erläuterungen Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/6-ueb-2.mp4" target="_blank" rel="noreferrer">Übungsaufgaben und Erläuterungen Teil 2</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-1.mp4" target="_blank" rel="noreferrer">Nachträge zu Potenzreihen</a>,&nbsp; <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/7-1-2.mp4" target="_blank" rel="noreferrer">Übungen zu Potenzreihen</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	Nicht klausurrelevant sind also 	<ul> 		<li>bei den Reihen (Abschnitt 3.2.) die Themen ab "Leibniz-Kriterium" (ab Satz 3.2.20),</li> 		<li>bei den Potenzreihen (Abschnitt 3.3) die Themen ab "Konvergenzradius" (ab Satz 3.3.9).<br> 		&nbsp;</li> 	</ul> 	</li> 	<li>(nicht klausurrelevante) Videos zur Eulerschen Summenformel zur Summe 1/k^2: 	<ul> 		<li><a href="https://www.youtube.com/watch?v=d-o3eB9sfls&amp;t=42s" title="Opens internal link in current window" target="_blank" rel="noreferrer">mit "Leuchttürmen"</a></li> 		<li><a href="https://www.youtube.com/watch?v=yPl64xi_ZZA" target="_blank" rel="noreferrer">mit Bezug zu Potenzreihen</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_2_Geometrische_Reihe.ggb" target="_blank" rel="noreferrer">Geometrische Reihe</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_2_Harmonische_Reihe.ggb" target="_blank" rel="noreferrer">Harmonische Reihe</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_3_Potenzreihe_exp.ggb" target="_blank" rel="noreferrer">Potenzreihe Exponentialfunktion</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_3_Potenzreihe_cos.ggb" target="_blank" rel="noreferrer">Potenzreihe Cosinus</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_3_Potenzreihe_sin.ggb" target="_blank" rel="noreferrer">Potenzreihe Sinus</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_2_GeometrischeReiheKomplex.ggb" target="_blank" rel="noreferrer">Geometrische Reihe mit komplexem q</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt6-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 6-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt6-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 6-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 6-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt6-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b3_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, zu Aufgabe 9 gibt es leider kein Video, <a href="https://www.hm-kompakt.de/video?watch=b3_2__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_2__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a></li> 		</ul> 		</li> 		<li>Übungsblatt 6-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt6-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b3_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_3__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_3__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_3__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304233" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            5. Woche (02.11. bis 09.11.): Folgen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304233">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304233">
                                <button aria-controls="accordion_content_304233" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304233" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304233" class="accordion-collapse collapse" id="accordion_content_304233">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304232" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitt 3.1. ab Beispiel 3.1.5 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag:&nbsp; <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-1.mp4" target="_blank" rel="noreferrer">Bemerkungen zu Übungsaufgaben</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-2.mp4" target="_blank" rel="noreferrer">Konvergenz-Definition</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-3.mp4" target="_blank" rel="noreferrer">Grenzwert-Sätze</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-4.mp4" target="_blank" rel="noreferrer">Grenzwerte bei rekursiv definierten Folgen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-5.mp4" target="_blank" rel="noreferrer">Uneigentliche Konvergenz</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-6.mp4" target="_blank" rel="noreferrer">Wichtige Grenzwerte</a></li> 			<li>vom Freitag: Ergänzungsveranstaltung zu komplexen Zahlen, s. 4. Woche</li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-ueb-2.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 2</a></li> 			<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 			&nbsp;</li> 		</ul> 		</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/3_1_e_pi.ggb" target="_blank" rel="noreferrer">e hoch j mal pi gleich -1</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Probeklausur: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/pklausur1.mp4" target="_blank" rel="noreferrer">Erläuterungen zum Klausurschreiben</a></li> 		<li><a href="http://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/pklausur1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Probeklausur </a></li> 		<li><a href="http://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/pklausur1_loes.pdf" target="_blank" rel="noreferrer">Musterlösung schriftlich</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/pklausur1_loes.mp4" target="_blank" rel="noreferrer">Musterlösung mündlich</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt5.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 5</a><br> 	&nbsp;</li> 	<li>Lösungen zum Übungsblatt: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt5_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 		<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b3_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a></li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304235" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            4. Woche (26.10. bis 02.11.): Komplexe Zahlen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304235">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304235">
                                <button aria-controls="accordion_content_304235" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304235" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304235" class="accordion-collapse collapse" id="accordion_content_304235">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304234" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 2.1 (Rest), 2.2&nbsp; und 2.3 sowie Abschnitt 3.1. bis Bemerkung 3.1.6 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-1-2.mp4" target="_blank" rel="noreferrer">konjugiert komplexe Zahl und Division</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-1-3.mp4" target="_blank" rel="noreferrer">komplexe "Wurzeln" und Polynome</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-2-1.mp4" target="_blank" rel="noreferrer">Organisatorische Ansagen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-2-2.mp4" target="_blank" rel="noreferrer">Polardarstellung</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-2-3.mp4" target="_blank" rel="noreferrer">Folgen - Einführung</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 1</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-ueb-2.mp4" target="_blank" rel="noreferrer">Übungsaufgaben Teil 2</a></li> 			<li>vom Donnerstag:&nbsp; <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-1-1.mp4" target="_blank" rel="noreferrer">Bemerkungen zu Übungsaufgaben</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Nicht klausurrelevante Ergänzung zu komplexen Zahlen: 	<ul> 		<li>Vorlesungsaufzeichnung:&nbsp;<a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-2-1.mp4" target="_blank" rel="noreferrer">Visualisierung komplexer Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-2-2.mp4" target="_blank" rel="noreferrer">die Funktion 1/z</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/5-2-3.mp4" target="_blank" rel="noreferrer">Mandelbrotmenge</a></li> 		<li>oder einzelne Videos: 		<ul> 			<li><a href="https://youtu.be/cKAooDwV5kk" target="_blank" rel="noreferrer">Einführung</a></li> 			<li><a href="https://youtu.be/2HoCYEaxrMk" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Teil 1 (Visualisierung komplexer Funktionen)</a></li> 			<li><a href="https://youtu.be/cOsYRng2YCQ" title="Opens internal link in current window" target="_blank" rel="noreferrer">Teil 2 (Die Funktion 1/z, "Greise" und die Riemannsche Zahlenkugel)</a></li> 			<li><a href="https://youtu.be/TreN03afCpc" target="_blank" rel="noreferrer">Teil 3 (Die Mandelbrot-Menge bzw. das Apfelmännchen)</a></li> 		</ul> 		</li> 		<li>Links/Applets dazu: 		<ul> 			<li><a href="https://www.mathe-online.at/nml/materialien/innsbruck/komplex2d/index.html" target="_blank" rel="noreferrer">Visualisierung komplexer Funktionen im 2-Dimensionalen</a></li> 			<li><a href="https://www.mathe-online.at/nml/materialien/innsbruck/komplex3d/index.html" target="_blank" rel="noreferrer">Visualisierung komplexer Funktionen im 3-Dimensionalen</a></li> 			<li><a href="https://vqm.uni-graz.at/pages/complex/index1.html" target="_blank" rel="noreferrer">Visualisierung komplexer Funktionen mit Farben</a></li> 			<li><a href="https://www.geogebra.org/m/VWSN8Rp7" target="_blank" rel="noreferrer">Visualisierung von 1/z</a></li> 			<li><a href="https://www.geogebra.org/m/gD7Rygd2" target="_blank" rel="noreferrer">Riemannsche Zahlenkugel</a></li> 			<li><a href="https://www.math.umn.edu/%7Earnold/moebius/" target="_blank" rel="noreferrer">Film: Möbius-Transformation</a></li> 			<li><a href="https://math.hws.edu/eck/js/mandelbrot/MB.html" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Mandelbrot-Menge (Apfelmännchen) als Applet</a></li> 			<li><a href="https://www.fractalizer.de/index.html" target="_blank" rel="noreferrer">Mandelbrot-Menge (Apfelmännchen) als Programm</a></li> 			<li><a href="https://www.youtube.com/watch?v=9M7dTmvJxwA" target="_blank" rel="noreferrer">Zoom in die Mandelbrot-Menge</a></li> 			<li>Mandelbear-Bilder: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Baer1.jpg" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">1</a> <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Baer2.jpg" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">2</a> <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Baer3.jpg" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">3</a><br> 			&nbsp;</li> 		</ul> 		</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexe_Addition.ggb" target="_blank" rel="noreferrer">Komplexe Addition</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexe_Multiplikation.ggb" target="_blank" rel="noreferrer">Komplexe Multiplikation</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexer_Kehrwert.ggb" target="_blank" rel="noreferrer">Komplexer Kehrwert</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexe_Wurzel.ggb" target="_blank" rel="noreferrer">Komplexe Wurzel</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_2_komplexe_Nullstellen.ggb" target="_blank" rel="noreferrer">Komplexe Nullstellen</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt4-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 4-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt4-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 4-2 </a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 4-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt4-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b2_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_2__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a></li> 		</ul> 		</li> 		<li>Übungsblatt 4-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt4-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b2_3__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_3__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b3_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304237" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            3. Woche (19.10. bis 26.10.): Umkehrfunktionen, Modifikationen von Funktionen; komplexe Zahlen Einführung
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304237">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304237">
                                <button aria-controls="accordion_content_304237" aria-expanded="false" class="accordion-button collapsed" data-bs-target="#accordion_content_304237" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304237" class="accordion-collapse collapse" id="accordion_content_304237">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304236" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 1.3, 1.4 und 2.1 (Beginn) 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen: 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-1.mp4" target="_blank" rel="noreferrer">Übungen zur Symmetrie</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-2.mp4" target="_blank" rel="noreferrer">Übungen zu in-/sur-/bijektiv</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-3.mp4" target="_blank" rel="noreferrer">Wurzel- und Arcus-Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-4.mp4" target="_blank" rel="noreferrer">Logarithmus</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-5.mp4" target="_blank" rel="noreferrer">logarithmische Skalierung</a></li> 			<li>vom Freitag:&nbsp;<a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-2-1.mp4" target="_blank" rel="noreferrer">Modifikation von Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-2-2.mp4" target="_blank" rel="noreferrer">Komplexe Zahlen - Einführung</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-ueb-1.mp4" target="_blank" rel="noreferrer">Übungsaufgaben zu Wurzeln, Arcus-Funktionen und Logarithmus</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-ueb-2.mp4" target="_blank" rel="noreferrer">Bodensee-Aufgabe</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/4-1-1.mp4" target="_blank" rel="noreferrer">Besprechung von Übungsaufgaben</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_4_4_Modifikation.ggb" target="_blank" rel="noreferrer">Funktions-Modifikation</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexe_Addition.ggb" target="_blank" rel="noreferrer">Komplexe Addition</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/2_1_komplexe_Multiplikation.ggb" target="_blank" rel="noreferrer">Komplexe Multiplikation</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt3-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 3-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt3-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 3-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 3-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt3-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_3_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_2__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_2__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_2__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_3__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_3__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_3__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_3__12" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_3__13" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_3_4__14" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 12</a></li> 		</ul> 		</li> 		<li>Übungsblatt 3-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt3-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_4__17" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__18" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__19" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__20" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, Aufgabe 5&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_4__21-1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Teil1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__21-2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Teil2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__21-3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Teil3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__21-4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Teil4</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__22" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__23" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_4__24" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b2_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304239" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            2. Woche (12.10. bis 19.10.): Exponentialfkt., trig. Fkt., Betragsfunktion; Eigenschaften von Funktionen
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304239">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304239">
                                <button aria-controls="accordion_content_304239" aria-expanded="true" class="accordion-button" data-bs-target="#accordion_content_304239" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304239" class="accordion-collapse collapse show" id="accordion_content_304239" style="">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304238" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Inhalt: Abschnitte 1.1.5 bis 1.1.7 sowie 1.2 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>Vorlesungsaufzeichnungen 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-1.mp4" target="_blank" rel="noreferrer">Besprechung des Übungsblatts: Polynome</a>,&nbsp;<a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-2.mp4" target="_blank" rel="noreferrer">Besprechung des Übungsblatts: gebr. rat. Fkt.</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-3.mp4" target="_blank" rel="noreferrer">trigonometrische Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-4.mp4" target="_blank" rel="noreferrer">Exponentialfunktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-5.mp4" target="_blank" rel="noreferrer">Betrags-Funktion</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-2-1.mp4" target="_blank" rel="noreferrer">Symmetrie</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-2-2.mp4" target="_blank" rel="noreferrer">Monotonie</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-2-3.mp4" target="_blank" rel="noreferrer">Umkehrbarkeit</a></li> 			<li>vom Montag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-ueb-1.mp4" target="_blank" rel="noreferrer">Übungen zu Winkelfunktionen und Additionstheoremen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-ueb-2.mp4" target="_blank" rel="noreferrer">Übungen zu hyperbolischen Funktionen</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-1.mp4" target="_blank" rel="noreferrer">Übungen zur Symmetrie</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/3-1-2.mp4" target="_blank" rel="noreferrer">Übungen zu in-/sur-/bijektiv</a></li> 		</ul> 		</li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a></li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Winkelfunktionen(sin,cos).ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Definition von Sinus/Cosinus im Einheitskreis</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Winkelfunktion%20(tan).ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Definition des Tangens</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Sinus_Punktsymmetrie.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Sinus Punktsymmetrie</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Sinus_Pi_Sym.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Sinus Symmetrie</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Cosinus_Achsensymmetrie.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Cosinus Achsensymmetrie</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_5_Cosinus_Pi_Sym.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Cosinus Symmetrie</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt2-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 2-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt2-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 2-2 </a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 2-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt2-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__6" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_5__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_6__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_6__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a>,&nbsp; <a href="https://www.hm-kompakt.de/video?watch=b1_1_6__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 10</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_7__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 11</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_7__12" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 12</a></li> 		</ul> 		</li> 		<li>Übungsblatt 2-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt2-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_2__13" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_2__14" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_2__15" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_2__16" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_2__17" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_2__18" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                               \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304241" class="frame frame-default frame-type-gridelements_pi1 frame-layout-0">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
               \s
                    <header>
                       \s
                        
               \s
                        <h2 class="">
                            1. Woche (05.10. bis 12.10.): Lineare und quadratische Funktionen, Polynome, gebr. rat. Fkt.
                        </h2>
                   \s
                        
                        
                        
                       \s
                        
                        
                        
                       \s
                        
                        
                        
                    </header>
               \s
                        
                        
                        
                           \s
                           \s
                    <div aria-multiselectable="false" class="accordion" id="accordion_304241">
                        <div class="accordion-item">
                            <div class="accordion-header" id="accordion_header_304241">
                                <button aria-controls="accordion_content_304241" aria-expanded="true" class="accordion-button" data-bs-target="#accordion_content_304241" data-bs-toggle="collapse" type="button">
                                    <div class="accordion-button-icon">
                                        <span></span>
                                        <span></span>
                                    </div>
                                    Details anzeigen
                                </button>
                            </div>
                            <div aria-labelledby="accordion_header_304241" class="accordion-collapse collapse show" id="accordion_content_304241" style="">
                                <div class="accordion-body">
                                   \s
                                       \s
                        
                        
                   \s
                       \s
               \s
                        
                        <div id="c304240" class="frame frame-default frame-type-text frame-layout-noAutoColumns">
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
               \s
                        <div class="largeText">
                            <ul> 	<li>Abschnitte 1.1.1 bis 1.1.4 	<ul> 		<li>aus dem <a href="/menschen/hoever/buecher/hoehere-mathematik-kompakt" title="Opens internal link in current window" class="internal-link">Buch "Höhere Mathematik kompakt"</a></li> 		<li>oder die entsprechenden Videos auf <a href="https://www.hm-kompakt.de" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">www.hm-kompakt.de</a><br> 		(in den Videos fehlt Bem. 1.1.9 und 1.1.10 des Buchs (3. Auflage))</li> 		<li>Aufzeichnung der Veranstaltung 		<ul> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/0-orga.mp4" target="_blank" rel="noreferrer">Organisation</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-1-1.mp4" target="_blank" rel="noreferrer">lineare Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-1-2.mp4" target="_blank" rel="noreferrer">quadratische Funktionen</a></li> 			<li>vom Freitag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-2-1.mp4" target="_blank" rel="noreferrer">Polynome</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-2-2.mp4" target="_blank" rel="noreferrer">gebr. rat. Funktionen</a></li> 			<li>vom Montag: Übungen/Vertiefungen zu <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-ueb-1.mp4" target="_blank" rel="noreferrer">linearen Funktionen</a>, <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/1-ueb-2.mp4" target="_blank" rel="noreferrer">quadratischen Funktionen</a></li> 			<li>vom Donnerstag: <a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-1.mp4" target="_blank" rel="noreferrer">Besprechung des Übungsblatts: Polynome</a>,&nbsp;<a href="https://www.hoever-downloads.fh-aachen.de/mathe1/Aufzeichnungen23/2-1-2.mp4" target="_blank" rel="noreferrer">Besprechung des Übungsblatts: gebr. rat. Fkt.</a></li> 		</ul> 		</li> 	</ul> 	</li> 	<li>Geogebra-Visualisierung: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_1_LineareFunktionen.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Lineare Funktion</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_2_Parabel_durch_3_Punkte.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Parabel durch 3 Punkte</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_2_Parabelfunktion_in_Normalform.ggb" title="Opens external link in new window" target="_blank" class="external-link-new-window" rel="noreferrer">Parabelfunktion in Normalform</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/hm-kompakt/visualisierungen/1_1_3_Interpolation_durch_Polynome.ggb" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Interpolation durch Polynome</a></li> 	</ul> 	</li> 	<li>Übungsblätter: 	<ul> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt1-1.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 1-1</a></li> 		<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/Blatt1-2.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">Übungsblatt 1-2</a><br> 		&nbsp;</li> 	</ul> 	</li> 	<li>Lösungen zu den Übungsblättern: 	<ul> 		<li>Übungsblatt 1-1: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt1-1_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_1_1__1" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_1__2" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_1__3" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_1__4" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__5" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__6a" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgbae 6a</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__6b" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6b</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__6c" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 6c</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__7" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 7</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_2__8" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a></li> 		</ul> 		</li> 		<li>Übungsblatt 1-2: 		<ul> 			<li><a href="https://www.hoever-downloads.fh-aachen.de/mathe1/ueb23/loes/Blatt1-2_Loes.pdf" title="Opens internal link in current window" target="_blank" rel="noreferrer">schriftlich</a></li> 			<li>als Videos: <a href="https://www.hm-kompakt.de/video?watch=b1_1_3__9" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 1</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_3__10" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 2</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_3__11" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 3</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_3__12" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 4</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_4__13" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 5</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_4__15" title="Opens internal link in current window" target="_blank" rel="noreferrer">Aufgabe 6</a>, Aufgabe 7 (da gibt's leider kein Video), <a href="https://www.hm-kompakt.de/video?watch=b1_1_4__16" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 8</a>, <a href="https://www.hm-kompakt.de/video?watch=b1_1_4__17" title="Opens internal link in current window" target="_blank" class="external-link-new-window" rel="noreferrer">Aufgabe 9</a></li> 		</ul> 		</li> 	</ul> 	</li> </ul>
                        </div>
                   \s
                        
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                                   \s
                                </div>
                            </div>
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
                        
                   \s
               \s
                        
                        
                           \s
                        </div>
                    </div>
               \s
                           \s
                               \s
                        
                        
                        
                           \s
                           \s
                               \s
                        
                        
                        
                           \s
                        </div>
                        
                   \s
                        
            </div></div></div>
               \s
                        
                        
                        
                <!--TYPO3SEARCH_end-->
                        
            </main>
                        
                <footer class="container-fluid">
                    <div class="fh-content-block mt-2 bg-dark text-light">
                        <div class="container">
                            <div class="row">
                               \s
                                    <div class="col col-lg-3">
                                        <div class="header">Karriere</div>
                                        <ul class="navbar-nav">
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/arbeiten/stellenanzeigen" target="_self">Stellenangebote der FH Aachen</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/arbeiten/berufsausbildung-an-der-fh-aachen" target="_self">Berufsausbildung an der FH Aachen</a>
                                                       \s
                                                </li>
                                           \s
                                        </ul>
                                    </div>
                               \s
                                    <div class="col col-lg-3">
                                        <div class="header">Presse</div>
                                        <ul class="navbar-nav">
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/hochschulstruktur/stabstellen/poem/redaktion" target="_self">Publikationen</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/hochschulstruktur/stabstellen/poem/redaktion/fuer-journalistinnen" target="_self">für Journalist:innen</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/rss" target="_self">RSS-Feed</a>
                                                       \s
                                                </li>
                                           \s
                                        </ul>
                                    </div>
                               \s
                                    <div class="col col-lg-3">
                                        <div class="header">Kontakt/Hilfe</div>
                                        <ul class="navbar-nav">
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/hochschulstruktur/zv/dez-2/ii2-studierendensekretariat" target="_self">Student-Service-Center (SSC)</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://jira.fh-aachen.de/servicedesk/customer/portal/4" target="_blank">IT-Support</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/fh-aachen/hochschulprofil/standort" target="_self">Service-Standorte</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.studierendenwerk-aachen.de" target="_blank">Wohnen und Gastro-Angebote</a>
                                                       \s
                                                </li>
                                           \s
                                        </ul>
                                    </div>
                               \s
                                    <div class="col col-lg-3">
                                        <div class="header">Sonstiges</div>
                                        <ul class="navbar-nav">
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/impressum" target="_self">Impressum</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/datenschutz" target="_self">Datenschutz</a>
                                                       \s
                                                </li>
                                           \s
                                                <li class="nav-item">
                                                   \s
                                                            <a class="nav-link text-light" href="https://www.fh-aachen.de/barrierefreiheit" target="_self">Barrierefreiheit</a>
                                                       \s
                                                </li>
                                           \s
                                        </ul>
                                    </div>
                               \s
                            </div>
                        </div>
                    </div>
                    <div class="container mt-2">
                       \s
            <div class="fh_partner row align-items-center justify-content-around">
                <div class="col text-center" title="Logo audit Familiengerechte Hochschule">
                    <a href="https://akkreditierungsrat.de/de/akkreditierungssystem/systemakkreditierung/systemakkreditierung/" target="_blank">
                        <img alt="Logo System Akkreditiert" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_akkreditierungsrat.png"></a>
                </div>
                <div class="col text-center" title="Logo audit Familiengerechte Hochschule">
                    <a href="https://www.berufundfamilie.de/" target="_blank">
                        <img alt="Logo audit Familiengerechte Hochschule" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_familiengerecht.png"></a>
                </div>
                <div class="col text-center" title="Logo HochschulAllianz für Angewandte Wissenschaften">
                    <a href="http://www.hawtech.de/" target="_blank">
                        <img alt="Logo HochschulAllianz für Angewandte Wissenschaften" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_HAW-tech.png"></a>
                </div>
                <div class="col text-center" title="Logo FutureLab Aachen">
                    <a href="http://www.futurelab-aachen.de/" target="_blank">
                        <img alt="Logo FutureLab Aachen" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_FutureLab.png"></a>
                </div>
                <div class="col text-center" title="Logo CHE-Ranking">
                    <a href="https://www.che.de/rankingsiegel " target="_blank">
                        <img alt="Logo CHE-Ranking" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_Ranking_Siegel_2022.png"></a>
                </div>
                <div class="col text-center" title="Logo DG HOCH N">
                    <a href="https://www.dg-hochn.de/" target="_blank">
                        <img alt="Logo DG HOCH N" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_DG_Hoch_N.png"></a>
                </div>
                <div class="col text-center" title="Logo Charta der Vielfalt">
                    <a href="https://www.charta-der-vielfalt.de" target="_blank">
                        <img alt="Logo Charta der Vielfalt" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_ChartaDerVielfalt.png"></a>
                </div>
                <div class="col text-center" title="Logo Observatory Magna Charta Universitatum">
                    <a href="http://www.magna-charta.org/" target="_blank">
                        <img alt="Logo Observatory Magna Charta Universitatum" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_MC.png"></a>
                </div>
                <div class="col text-center" title="Logo Familie in der Hochschule">
                    <a href="https://www.familie-in-der-hochschule.de/" target="_blank">
                        <img alt="Logo Familie in der Hochschule" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_FidH.jpg"></a>
                </div>
                <div class="col text-center" title="Zertifikat 2022 – Vielfalt gestalten">
                    <a href="https://www.stifterverband.org/diversity-audit" target="_blank">
                        <img alt="Zertifikat 2022 – Vielfalt gestalten" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_Vielfalt_gestalten_2022.png"></a>
                </div>
                <div class="col text-center" title="Erasmus +">
                    <a href="https://www.fh-aachen.de/studium/studieren/auslandsaufenthalte/foerdermoeglichkeiten/erasmus" target="_blank">
                        <img alt="Erasmus + Enriching lives, opening minds" width="140" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_erasmusPlus.png"></a>
                </div>
                <div class="col text-center" title="Fairtrade University">
                    <a href="https://www.fairtrade-universities.de/" target="_blank">
                        <img alt="Fairtrade Universities" width="140" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_fairtrade.png"></a>
                </div>
                <div class="col text-center" title="Total E-Quality">
                    <a href="https://www.total-e-quality.de/de/logo/" target="_blank">
                        <img alt="Total E-Quality" width="140" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_TEQ.png"></a>
                </div>
                <div class="col text-center" title="Typo3 Academic Silver Member">
                    <a href="https://www.typo3.org" target="_blank">
                        <img alt="Typo3 Academic Silver Member" width="140" src="/typo3conf/ext/fhac_design_2021/Resources/Public/Images/Partner/logo_academic_silver.1c01a0e2.svg"></a>
                </div>
            </div>
                        
                        
                    </div>
                    <button id="toTopBtn" onclick="topFunction()" title="Go to top" style="display: block;"><i aria-hidden="true" class="bi bi-chevron-up"></i></button>
                </footer>
                        
                        
                        
            <script>
                var _paq = window._paq = window._paq || [];
                /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
                _paq.push(["setCookieDomain", "*.fh-aachen.de"]);
                _paq.push(["setExcludedQueryParams", ["no_cache"]]);
                _paq.push(['trackPageView']);
                _paq.push(['enableLinkTracking']);
                (function () {
                    var u = "https://stats.fh-aachen.de/";
                    _paq.push(['setTrackerUrl', u + 'matomo.php']);
                    _paq.push(['setSiteId', '10']);
                    var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
                    g.async = true;
                    g.src = u + 'matomo.js';
                    s.parentNode.insertBefore(g, s);
                })();
            </script>
            <noscript><p><img alt="" src="https://stats.fh-aachen.de/matomo.php?idsite=10&amp;rec=1"
                              style="border:0;"/></p></noscript>
            <!-- End Matomo Code -->
            <script src="/typo3temp/assets/compressed/bootstrap.bundle.min-8864a21c6e1b799b6fd3095f3a7e7043.js.gzip?1707465595"></script>
            <script src="/typo3temp/assets/compressed/cookieConfig-1fc59f3b4790b179a332777f685dad42.js.gzip?1708585240"></script>
            <script src="/typo3temp/assets/compressed/in2cookiemodal-9b9f339966ee355c7e80ca6a8fcff570.js.gzip?1707465595"></script><div class="in2-modal__blackbox" style="display: none;" data-in2-modal-root-node="">
                <div class="in2-modal">
                    <div class="in2-modal-container">
                        <h1 class="in2-modal-heading">
                            Datenschutzeinstellungen
                        </h1>
                        <p class="in2-modal-paragraph">
                            Auf unserer Internetseite werden Cookies verwendet. Einige davon werden zwingend benötigt, während andere es uns ermöglichen, Ihre Nutzerinnen- und Nutzererfahrung auf unserer Internetseite zu verbessern.
                        </p>
                    </div>
            <div class="in2-modal-spacing in2-modal-flexline in2-modal-flexline--column in2-modal-container in2-modal-container--big">
                        <button class="in2-modal-reset in2-modal-button" tabindex="100" data-in2-modal-accept-button="">
                Alle akzeptieren
            </button>
            <p class="in2-modal-divider">
                oder
            </p>
                        
                    </div>
                    <div class="in2-modal-flexline in2-modal-spacing in2-modal-container in2-modal-container--big">
                       \s
                            <div class="in2-modal-reset in2-modal-checkbox">
                <input class="in2-modal-reset in2-modal-checkbox__input" disabled="" checked="" type="checkbox" value="essential" id="in2-modal-checkbox-essential" tabindex="100">
                <label class="in2-modal-reset in2-modal-checkbox__label" for="in2-modal-checkbox-essential">Essentiell</label>
            </div>
                        
                       \s
                            <div class="in2-modal-reset in2-modal-checkbox">
                <input class="in2-modal-reset in2-modal-checkbox__input" checked="" type="checkbox" value="analytics" id="in2-modal-checkbox-analytics" tabindex="100">
                <label class="in2-modal-reset in2-modal-checkbox__label" for="in2-modal-checkbox-analytics">lokale Web-Analyse</label>
            </div>
                        
                       \s
                            <div class="in2-modal-reset in2-modal-checkbox">
                <input class="in2-modal-reset in2-modal-checkbox__input" checked="" type="checkbox" value="search" id="in2-modal-checkbox-search" tabindex="100">
                <label class="in2-modal-reset in2-modal-checkbox__label" for="in2-modal-checkbox-search">Suche</label>
            </div>
                        
                       \s
                    </div>
                    <div class="in2-modal-spacing in2-modal-flexline in2-modal-flexline--column in2-modal-container in2-modal-container--big">
                        <button class="in2-modal-reset in2-modal-button " tabindex="100" data-in2-modal-save-button="">
                Speichern &amp; Schließen
            </button>
                        
                    </div>
                    <dl class="in2-modal-accordion in2-modal-container in2-modal-container--big">
                       \s
                            <dt class="in2-modal-accordion__title in2-modal-spacing" data-role="accordiontrigger" tabindex="100">
                <p>Essentiell</p>
                <span class="in2-modal-accordion__icon"></span>
            </dt>
            <dd class="in2-modal-accordion__content">
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>fhac_cookiemodal-selection</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>FH Aachen</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>zum Speichern der Benutzerauswahl der Cookieeinstellungen erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>10 Jahre</p>
                </div>
            </div>
                        
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>fe_typo_user</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>FH Aachen</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>für die Anmeldung auf www.fh-aachen.de erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>Session</p>
                </div>
            </div>
                        
               \s
            </dd>
                        
                       \s
                            <dt class="in2-modal-accordion__title in2-modal-spacing" data-role="accordiontrigger" tabindex="100">
                <p>lokale Web-Analyse</p>
                <span class="in2-modal-accordion__icon"></span>
            </dt>
            <dd class="in2-modal-accordion__content">
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>_pk_ses</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>FH Aachen - Webstatistik</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>für die Web-Analyse auf dem Matomo-Server der FH Aachen erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>30 Minuten</p>
                </div>
            </div>
                        
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>_pk_id</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>FH Aachen - Webstatistik</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>für die Web-Analyse auf dem Matomo-Server der FH Aachen erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>13 Monate</p>
                </div>
            </div>
                        
               \s
            </dd>
                        
                       \s
                            <dt class="in2-modal-accordion__title in2-modal-spacing" data-role="accordiontrigger" tabindex="100">
                <p>Suche</p>
                <span class="in2-modal-accordion__icon"></span>
            </dt>
            <dd class="in2-modal-accordion__content">
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>NID</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>Google</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>für die Suche auf der Seite erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>6 Monate</p>
                </div>
            </div>
                        
               \s
                    <div class="in2-modal-accordion__cookie">
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Name</strong>
                    <p>CONSENT</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Anbieter</strong>
                    <p>Google</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Zweck</strong>
                    <p>für die Suche auf der Seite erforderlich</p>
                </div>
                <div class="in2-modal-accordion__itempoint">
                    <strong class="in2-modal-accordion__strong">Gültigkeit</strong>
                    <p>unbegrenzt</p>
                </div>
            </div>
                        
               \s
            </dd>
                        
                       \s
                    </dl>
                    <div class="in2-modal-flexline in2-modal-flexline--center in2-modal-spacing in2-modal-container">
                       \s
            	<a class="in2-modal-link in2-modal-link--highlight" href="/impressum/?in2cookieHideOptIn">
            		Impressum
            	</a>
            	
            		<span>|</span>
            	
                        
            	<a class="in2-modal-link in2-modal-link--highlight" href="/datenschutz/?in2cookieHideOptIn">
            		Datenschutzerklärung
            	</a>
            	
                        
                        
                    </div>
                </div>
            </div>
            <script src="/typo3temp/assets/compressed/eyeAbleInclude-868ecf5a24138f8900063c89c2ae391e.js.gzip?1707465595" async="async"></script>
            <script src="https://eye-able.fh-aachen.de/public/js/eyeAble.js" async="async"></script>
            <script src="/typo3temp/assets/compressed/Form.min-85db7fe7c300cb1a9ae3536b5b9db626.js.gzip?1707463681" defer="defer"></script>
            <script src="/typo3temp/assets/compressed/scripts-428773c7fa5410815f4a27e6a5732f6b.js.gzip?1707465595"></script>
                        
                        
                        
            <table cellspacing="0" cellpadding="0" style="width: 1304px; display: none; top: 157px; position: absolute; left: 1049px;" role="presentation" class="gstl_50 gssb_c"><tbody><tr><td class="gssb_f"></td><td class="gssb_e" style="width: 100%;"></td></tr></tbody></table><div class="eyeAble_container_b" id="eyeAble_container_ID" aria-label="Eye-Able Assistenztechnik. Öffnen mit ALT und 1. Bedienung mit TAB. Verlassen mit Esc." role="complementary"></div></body>
            """;
}

