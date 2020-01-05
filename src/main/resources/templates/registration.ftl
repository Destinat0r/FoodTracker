<#import "parts/common.ftl" as c>
<#import "parts/form_macroses.ftl" as form>

<@c.page>
<div class="mb-1">Add new user</div>

    ${message!}

    <@form.login "/registration" true/>
</@c.page>