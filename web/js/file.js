let organisation;
let period;
let listOrganisationEXPERIENCE = new Set()
let listOrganisationEDUCATION = new Set()
const numberOfOrganisation = new Map()
const organ = document.querySelectorAll('.organisations');

for (const x of organ) {
    const name = x.getAttribute('data-name')
    const number = parseInt(x.getAttribute('data-organisation'))
    numberOfOrganisation.set(name, number - 1)
    for (let i = 0; i < number; i++) {
        if (name === 'EDUCATION') {
            listOrganisationEDUCATION.add(i)
        } else {
            listOrganisationEXPERIENCE.add(i)
        }
    }
}

let firstOrganisation = new Map()
firstOrganisation.set("EXPERIENCE", listOrganisationEXPERIENCE)
firstOrganisation.set("EDUCATION", listOrganisationEDUCATION)
const numberOfPeriods = new Map()
const per = document.querySelectorAll('.periods');

for (const x of per) {
    numberOfPeriods.set(x.getAttribute('data-name') + x.getAttribute('data-organisation'), parseInt(x.getAttribute('data-period')) - 1);
}

function handleClickPeriods(event) {
    const nameSection = event.target.dataset.name;
    organisation = event.target.dataset.organisation
    period = numberOfPeriods.get(nameSection + organisation);
    periodFunction(nameSection, '#p-' + nameSection + organisation + period);
    const counterPeriods = document.querySelector('#counterPeriods' + nameSection + organisation);
    counterPeriods.value = (numberOfPeriods.get(nameSection + organisation) + 1) + "";

}


function handleClickOrganisations(event) {
    const nameSection = event.target.dataset.name;
    let set = firstOrganisation.get(nameSection)
    organisation = numberOfOrganisation.get(nameSection) + 1;
    set.add(parseInt(organisation));
    numberOfOrganisation.set(nameSection, organisation)
    numberOfPeriods.set(nameSection + organisation, -1);
    let div = document.createElement('div');
    div.id = ('o-' + nameSection + organisation);
    const organisationName = '<p> Organisation : <input type="text" name= title' + nameSection + organisation + ' size=30></p>';
    const siteName = '<p> Site : <input type="text" name= site' + nameSection + organisation + ' size=30></p>';
    div.innerHTML = organisationName + siteName;
    const node = document.querySelector('#b-o-r-' + nameSection + (organisation - 1));
    node.after(div);
    periodFunction(nameSection, '#o-' + nameSection + organisation);
    let divButton = document.createElement('div');
    divButton.id = ('b-p-' + nameSection + organisation);
    divButton.innerHTML = '<button type="button" id=' + 'b-p-i-' + nameSection + organisation +
        ' data-organisation=' + organisation + ' class="periods" data-period=' + 0 + ' data-name=' + nameSection + '>Добавить период</button>' +
        ' <input type="hidden" id="counterPeriods' + nameSection + organisation + '" name="counterPeriods' + nameSection + organisation + '" value =' + period + ' > ';
    const nodeButton = document.querySelector('#p-' + nameSection + organisation + period)
    nodeButton.after(divButton)
    let divRemoveOrganisation = document.createElement('div');
    divRemoveOrganisation.id = ('b-o-r-' + nameSection + organisation);
    divRemoveOrganisation.innerHTML = '<button id=' + 'b-o-rb-' + nameSection + organisation + ' data-organisation=' + organisation + ' type="button" ' +
        'class="removeOrganisation" data-name=' + nameSection + ' title=' + organisation + '>Удалить организацию</button>'
    const buttonRemove = document.querySelector('#b-p-' + nameSection + organisation)
    buttonRemove.after(divRemoveOrganisation)
    const buttonRemoveOrganisation = document.querySelector('#b-o-rb-' + nameSection + organisation)
    buttonRemoveOrganisation.addEventListener('click', removeOneOrganisation)
    const button = document.querySelector('#b-p-i-' + nameSection + organisation)
    button.addEventListener('click', handleClickPeriods)
    const counter = document.querySelector('#count' + nameSection);
    counter.value = parseInt(organisation) + 1;
    const counterPeriods = document.querySelector('#counterPeriods' + nameSection + organisation);
    counterPeriods.value = (numberOfPeriods.get(nameSection + organisation) + 1) + "";
    const setterFirstOrganisation = document.querySelector('#firstOrganisation' + nameSection);
    setterFirstOrganisation.value = Math.min.apply(this, [...set]);
}

function periodFunction(nameSection, str) {
    period = numberOfPeriods.get(nameSection + organisation);
    numberOfPeriods.set(nameSection + organisation, ++period);
    let div = document.createElement('div');
    div.id = 'p-' + nameSection + organisation + period;
    const start = '<p> Start : <input type="date" name= startDate' + nameSection + organisation + period + '>';
    const end = ' End : <input type="date" name= endDate' + nameSection + organisation + period + '></p>';
    const position = '<p> Position :  <input type="text" name= titlePeriod' + nameSection + organisation + period + '></p>';
    const description = '<textarea name=description' + nameSection + organisation + period + ' cols="200"></textarea>';
    let button = '<button type="button" class="removePeriod" id=p-b-r-' + nameSection + organisation + (period - 1) +
        ' data-name=' + nameSection + ' data-period=' + period + ' data-organisation=' + organisation + '>Удалить период</button>'
    if (period === 0) {
        button = ''
    }
    div.innerHTML = start + end + position + description + button;
    const node = document.querySelector(str);
    node.after(div);
    if (button !== '') {
        const buttonRemove = document.querySelector('#p-b-r-' + nameSection + organisation + (period - 1))
        buttonRemove.addEventListener('click', removePeriod)
    }
}

function removePeriod(event) {
    organisation = event.target.dataset.organisation
    const nameSection = event.target.dataset.name
    period = event.target.dataset.period
    const div = document.createElement('div')
    div.id = 'p-' + nameSection + organisation + period
    const block = document.querySelector('#p-' + nameSection + organisation + period)
    block.after(div)
    block.remove()
}

function removeOrganisation(event, organisation) {
    const nameSection = event.target.dataset.name
    const divOrganisation = document.createElement('div')
    divOrganisation.id = 'o-' + nameSection + organisation
    let block = document.querySelector('#o-' + nameSection + organisation)
    block.after(divOrganisation)
    block.remove()
    for (let i = 0; i < numberOfPeriods.get(nameSection + organisation) + 1; i++) {
        const divPeriod = document.createElement('div')
        divPeriod.id = 'p-' + nameSection + organisation + i
        const block1 = document.querySelector('#p-' + nameSection + organisation + i)
        block1.after(divPeriod)
        block1.remove()
    }
    const divAddPeriod = document.createElement('div')
    divAddPeriod.id = 'b-p-' + nameSection + organisation
    block = document.querySelector('#b-p-' + nameSection + organisation)
    block.after(divAddPeriod)
    block.remove()
    const divRemoveOrganisation = document.createElement('div')
    divRemoveOrganisation.id = 'b-o-r-' + nameSection + organisation
    block = document.querySelector('#b-o-r-' + nameSection + organisation)
    block.after(divRemoveOrganisation)
    block.remove()
    let set = firstOrganisation.get(nameSection)
    set.delete(parseInt(organisation));
    const setterFirstOrganisation = document.querySelector('#firstOrganisation' + nameSection);
    if (set.size !== 0) {
        setterFirstOrganisation.value = Math.min.apply(this, [...set]);
    }
}

function removeOneOrganisation(event) {
    organisation = event.target.dataset.organisation
    removeOrganisation(event, organisation)
}

function removeAllOrganisations(event) {
    const nameSection = event.target.dataset.name
    const checkbox = document.querySelector('#checkbox' + nameSection)
    let set = firstOrganisation.get(nameSection)
    organisation = Math.min.apply(this, [...set])
    let block = document.querySelector('#button-addOrganisation-' + nameSection)
    if (checkbox.checked) {
        const numberOrg = organisation + set.size
        for (let i = organisation; i < numberOrg; i++) {
            removeOrganisation(event, i);
        }
        period = numberOfPeriods.get(nameSection + organisation);
        numberOfPeriods.set(nameSection + organisation, 1);
        block.hidden = true
    } else {
        handleClickOrganisations(event)
        block.hidden = false
    }
}

textarea = document.querySelectorAll(".resizable");
for (const x of textarea) {
    x.addEventListener('input', autoResize, false);
}

for (const x of textarea) {
    x.querySelector('#' + x.getAttribute('name'))
    x.style.height = 'auto';
    x.style.height = x.scrollHeight + 'px';
}

function autoResize() {
    this.style.height = 'auto';
    this.style.height = this.scrollHeight + 'px';
}

const buttons = document.querySelectorAll('.periods');
for (const x of buttons) {
    x.addEventListener('click', handleClickPeriods);
}

const organisationButtons = document.querySelectorAll('.organisations');
for (const x of organisationButtons) {
    x.addEventListener('click', handleClickOrganisations);
}
const removePeriodButton = document.querySelectorAll('.removePeriod')
for (const x of removePeriodButton) {
    x.addEventListener('click', removePeriod)
}
const removeOrganisationButton = document.querySelectorAll('.removeOrganisation')
for (const x of removeOrganisationButton) {
    x.addEventListener('click', removeOneOrganisation)
}

const checkbox = document.querySelectorAll('.checkbox')
for (const x of checkbox) {
    x.addEventListener('click', removeAllOrganisations)
}