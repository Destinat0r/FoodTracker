<#import "parts/common.ftl" as c>

<@c.page>
    <form action="" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="User name" required/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="Email" required/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Full name:</label>
            <div class="col-sm-6">
                <input type="text" name="fullName" class="form-control" placeholder="Full name" required/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">National name:</label>
            <div class="col-sm-6">
                <input type="text" name="nationalName" class="form-control" placeholder="National name" required/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password" />
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password Confirmation:</label>
            <div class="col-sm-6">
                <input type="password" name="passwordConfirmation" class="form-control" placeholder="Password" />
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Create</button>
    </form>
</@c.page>