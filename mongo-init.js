db = db.getSiblingDB('test');

db.accounts.insertOne({
    _id: ObjectId(),
    accountNumber: 'NL91RABO0123456789',
    accountHolderName: 'John Doe',
    balance: 1000.0,
    accountType: 'PAYMENT',
    createdAt: new Date(),
    updatedAt: new Date()
});

db.power_of_attorneys.insertOne({
    _id: ObjectId(),
    _class: 'nl.rabobank.mongo.document.PowerOfAttorneyDocument',
    granteeName: 'didem',
    grantorName: 'John Doe',
    authorization: 'READ',
    account: {
        accountNumber: 'NL91RABO0123456789',
        accountHolderName: 'John Doe',
        balance: 1000.0,
        _class: 'nl.rabobank.account.PaymentAccount'
    },
    createdAt: new Date(),
    updatedAt: new Date()
});