<#import "parts/common.ftl" as c>
<#import "parts/form_macroses.ftl" as form>

<@c.page>
    <h3>Hello, guest!</h3>
    <div>Welcome to Food Tracker</div>
    <@form.login "/login" false/>
</@c.page>