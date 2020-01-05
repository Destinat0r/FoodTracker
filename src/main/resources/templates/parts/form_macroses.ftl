<#macro login path isRegisterForm>
    <form action="${path}" method="post" novalidate>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> User Name : </label>
            <div class="col-sm-6">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="Username" required/>

            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
            </div>
        </div>

<#if isRegisterForm>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Email: </label>
            <div class="col-sm-6">
                <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="Email" required/>

            <#if emailError??>
                <div class="invalid-feedback">
                    ${emailError}
                </div>
            </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Full name:</label>
            <div class="col-sm-6">
                <input type="text" name="fullName" value="<#if user??>${user.fullName}</#if>"
                       class="form-control ${(fullNameError??)?string('is-invalid', '')}"
                       placeholder="Full name" required/>

                <#if fullNameError??>
                    <div class="invalid-feedback">
                        ${fullNameError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">National name:</label>
            <div class="col-sm-6">
                <input type="text" name="nationalName" value="<#if user??>${user.nationalName}</#if>"
                       class="form-control ${(nationalNameError??)?string('is-invalid', '')}"
                       placeholder="National name" required/>

                <#if nationalNameError??>
                    <div class="invalid-feedback">
                        ${nationalNameError}
                    </div>
                </#if>
            </div>
        </div>
</#if>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password" />

                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

<#if isRegisterForm>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password Confirmation:</label>
            <div class="col-sm-6">
                <input type="password" name="passwordConfirm" class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
                       placeholder="Password" />

                <#if passwordConfirmError??>
                    <div class="invalid-feedback">
                        ${passwordConfirmError}
                    </div>
                </#if>
            </div>
        </div>
</#if>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit" class="btn btn-primary mb-2"><#if isRegisterForm>Create<#else >Sign In</#if></button>

        <#if !isRegisterForm>
            <a href="/registration">Registration</a>
        </#if>
    </form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Sign Out</button>
</form>
</#macro>